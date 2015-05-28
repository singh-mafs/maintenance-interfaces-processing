package com.mikealbert.batch.readers;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

/**
 * This can be used with Valvoline only until it is fixed to become truly generic
 * @author Mohan
 *
 */
public class MultiLineReader implements ItemReader<VendorInvoiceHeaderVO> {
	private static MalLogger log = MalLoggerFactory
			.getLogger(MultiLineReader.class);
	private boolean recordFinished;
	private ItemReader<FieldSet> fieldSetReader;
	private FieldSetMapper<VendorInvoiceHeaderVO> headerMapper;
	private FieldSetMapper<VendorInvoiceDetailsVO> detailsMapper;

	private VendorInvoiceHeaderVO record;
	private String currentLineId = null;
	private boolean isNewRecInitiated = false;
	private	FieldSet	fieldSetToStartOver = null;
	
	@Value("${coupons.code}")
	private	String	couponCode;
	
	@Value("${coupons.codedesc}")
	private	String	couponDesc;

	private	VendorInvoiceHeaderVO nextRecord;
	public VendorInvoiceHeaderVO read() throws Exception {
		recordFinished = false;
		do{
			process(fieldSetReader.read());
		}
		while (!recordFinished && currentLineId != null) ;
		if(record != null ){
			log.info("Mapped: " +  record.getPoNbr());
		}
		VendorInvoiceHeaderVO result = record;
		record = null;
		currentLineId = null;
		return result;
	}

	private void process(FieldSet fieldSet) throws Exception {
		// finish processing if we hit the end of file
		if (fieldSet == null) {
			if(fieldSetToStartOver != null){
				//ending with single line invoice
				fieldSet = fieldSetToStartOver;
				fieldSetToStartOver = null;
				isNewRecInitiated = false;
			}else{
				//end of file,return no more read
				log.info("FINISHED");
				recordFinished = true;
				return;
			}
			
		}

		String lineId = fieldSet.readString(0);
		if (currentLineId == null) {
			currentLineId = lineId;
		}

		// start a new record
		if (currentLineId.equals(lineId)) {
			boolean	isSingleLine = false;
			if(fieldSetToStartOver != null){
				FieldSet	tempRec = null;
				String lineIdOfStartOverRec = fieldSetToStartOver.readString(0);
				if(!lineIdOfStartOverRec.equals(currentLineId)){
					//single line record
					tempRec = fieldSet;
					fieldSet	= fieldSetToStartOver;
					currentLineId = null;
					fieldSetToStartOver = tempRec;
					isSingleLine = true;
					
				}
				
				if(nextRecord != null){
					record	= nextRecord;
					nextRecord = null;
				}
				if(isSingleLine){
					//This is single line invoice, so all details will be created using this single line 
					record =  headerMapper.mapFieldSet(fieldSet);
					record.setDetails(new ArrayList<VendorInvoiceDetailsVO>());
					VendorInvoiceDetailsVO details = detailsMapper.mapFieldSet(fieldSet);
					record.getDetails().add(details);
					prepareOneTimeDetailLines(fieldSet);	
				}else{
					//New record is not initiated, here we need to add header and one time detail lines for each invoice
					if(record == null){
						record =  headerMapper.mapFieldSet(fieldSetToStartOver);
						VendorInvoiceDetailsVO details = detailsMapper.mapFieldSet(fieldSetToStartOver);
						record.getDetails().add(details);
					}
					prepareOneTimeDetailLines(fieldSetToStartOver);
				}
				
				isNewRecInitiated = true;
				fieldSetToStartOver = tempRec;
			}
			if (!isNewRecInitiated) {
				record =  headerMapper.mapFieldSet(fieldSet);
				isNewRecInitiated = true;
				record.setDetails(new ArrayList<VendorInvoiceDetailsVO>());
				prepareOneTimeDetailLines(fieldSet);
				log.info("STARTING NEW HEADER:"+fieldSet.readString("INVNUM"));
			}
			if (isNewRecInitiated && !isSingleLine)  {
				//new record is initiated and header is already added,now it has more than 1 line, add detail line for each line
				log.info("STARTING NEW DETAIL:"+fieldSet.readString("INVNUM"));
				VendorInvoiceDetailsVO details = detailsMapper.mapFieldSet(fieldSet);
				record.getDetails().add(details);
				
			}
			isSingleLine = false;

		} else {
			log.info("END OF ONE RECORD GROUP");
			//One group of invoice is already read, encountered different invoice line, so it states that already created record will be returned.
			//Also prepare header and store it in nextRecord and and this nextRecord will become active record in next line read.
			//This different new invoice line will not be available in next read, so by creating nextRecord we will not miss it.
			isNewRecInitiated = false;
			recordFinished = true;
			fieldSetToStartOver	= fieldSet;
			nextRecord =  headerMapper.mapFieldSet(fieldSet);
			nextRecord.setDetails(new ArrayList<VendorInvoiceDetailsVO>());
			VendorInvoiceDetailsVO details = detailsMapper.mapFieldSet(fieldSet);
			nextRecord.getDetails().add(details);
		}

	}

	private void prepareOneTimeDetailLines(FieldSet fieldSet) {
		if (!MALUtilities.isEmpty(fieldSet.readString("TOTALTAXAMT"))) {
			if (MALUtilities.isNumeric(fieldSet.readString("TOTALTAXAMT"))) {
				BigDecimal checkAmt = new BigDecimal(fieldSet.readString("TOTALTAXAMT"));
				if (BigDecimal.ZERO.compareTo(checkAmt) != 0) {
					VendorInvoiceDetailsVO taxDetail = new VendorInvoiceDetailsVO();
					taxDetail.setTaxAmount(getRoundedValue(new BigDecimal(fieldSet.readString("TOTALTAXAMT")), 2).toString());
					taxDetail.setRecordType("D");
					record.getDetails().add(taxDetail);
				}
			}

		}
		if (!MALUtilities.isEmpty(fieldSet.readString("FLTTOTALDISCAMT"))) {
			if (MALUtilities.isNumeric(fieldSet.readString("FLTTOTALDISCAMT"))) {
				BigDecimal checkAmt = new BigDecimal(fieldSet.readString("FLTTOTALDISCAMT"));
				if (BigDecimal.ZERO.compareTo(checkAmt) != 0) {
					VendorInvoiceDetailsVO rebateDetail = new VendorInvoiceDetailsVO();
					rebateDetail.setRecordType("D");
					rebateDetail.setPartServiceDesc(fieldSet.readString("ITEMDESC"));
					if (!MALUtilities.isEmpty(fieldSet.readString("QTY"))) {
						rebateDetail.setQty(getRoundedValue(new BigDecimal(fieldSet.readString("QTY")), 2).toString());
					}else{
						rebateDetail.setQty("1.00");
					}
					rebateDetail.setPartServiceDesc(fieldSet.readString("ITEMDESC"));
					rebateDetail.setDiscRebateAmt(getRoundedValue(new BigDecimal(fieldSet.readString("FLTTOTALDISCAMT")), 2).toString());
					rebateDetail.setUnitCost(!MALUtilities.isEmpty(fieldSet.readString("LINEAMT")) ? getRoundedValue(
							new BigDecimal(fieldSet.readString("LINEAMT")), 2).toString() : null);
					rebateDetail.setTotalCost(!MALUtilities.isEmpty(fieldSet.readString("RTLPRICE")) ? getRoundedValue(
							new BigDecimal(fieldSet.readString("RTLPRICE")), 2).toString() : null);

					record.getDetails().add(rebateDetail);
				}
			}

		}
		if (!MALUtilities.isEmpty(fieldSet.readString("INVDISCAMT"))) {
			if (MALUtilities.isNumeric(fieldSet.readString("INVDISCAMT"))) {
				BigDecimal checkAmt = new BigDecimal(fieldSet.readString("INVDISCAMT"));
				if (BigDecimal.ZERO.compareTo(checkAmt) != 0) {
					VendorInvoiceDetailsVO creditDetail = new VendorInvoiceDetailsVO();
					creditDetail.setRecordType("D");
					/*if (!MALUtilities.isEmpty(fieldSet.readString("QTY"))) {
						creditDetail.setQty(getRoundedValue(new BigDecimal(fieldSet.readString("QTY")), 2).toString());
					}else{
						creditDetail.setQty("1.00");
					}*/
					creditDetail.setQty("1.00");
					if(checkAmt.compareTo(BigDecimal.ZERO) > 0){
						//negate this amount
						checkAmt	= checkAmt.negate();
					}
					checkAmt	= getRoundedValue(checkAmt,2);
					creditDetail.setUnitCost(checkAmt.toString());
					creditDetail.setTotalCost(checkAmt.toString());
					creditDetail.setDiscRebateAmt(checkAmt.toString());
					
					creditDetail.setPartServiceCode(couponCode);
					creditDetail.setPartServiceDesc(couponDesc);
					record.getDetails().add(creditDetail);
				}
			}
		}
	}

	private   BigDecimal getRoundedValue(BigDecimal input, int scale){
    	if(input != null){
    		   return input.setScale(scale, BigDecimal.ROUND_HALF_UP);
    	}else{
    		//return null;
    		return BigDecimal.ZERO.setScale(scale);
    	}
	 
	}
	public ItemReader<FieldSet> getFieldSetReader() {
		return fieldSetReader;
	}

	public void setFieldSetReader(ItemReader<FieldSet> fieldSetReader) {
		this.fieldSetReader = fieldSetReader;
	}

	public FieldSetMapper<VendorInvoiceHeaderVO> getHeaderMapper() {
		return headerMapper;
	}

	public void setHeaderMapper(
			FieldSetMapper<VendorInvoiceHeaderVO> headerMapper) {
		this.headerMapper = headerMapper;
	}

	public FieldSetMapper<VendorInvoiceDetailsVO> getDetailsMapper() {
		return detailsMapper;
	}

	public void setDetailsMapper(
			FieldSetMapper<VendorInvoiceDetailsVO> detailsMapper) {
		this.detailsMapper = detailsMapper;
	}

	

}
