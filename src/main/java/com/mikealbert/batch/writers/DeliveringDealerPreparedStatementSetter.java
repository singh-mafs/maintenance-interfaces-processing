package com.mikealbert.batch.writers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import com.mikealbert.batch.mappers.DeliveringDealerMapper;
import com.mikealbert.util.MALUtilities;

public class DeliveringDealerPreparedStatementSetter implements ItemPreparedStatementSetter<DeliveringDealerMapper> {
 
    public void setValues(DeliveringDealerMapper result, PreparedStatement ps) throws SQLException {
    	
        ps.setString(1, result.getMakeCode());
        ps.setString(2, !MALUtilities.isEmpty(result.getArea()) ? result.getArea() : null );
        ps.setString(3, !MALUtilities.isEmpty(result.getAreaDescription()) ? result.getAreaDescription() : null);
        ps.setString(4, !MALUtilities.isEmpty(result.getContactName()) ? result.getContactName() : null );
        ps.setString(5, !MALUtilities.isEmpty(result.getTedContactPhone()) ? result.getTedContactPhone() : null);
        ps.setString(6, !MALUtilities.isEmpty(result.getEmail()) ? result.getEmail() : null);
        ps.setString(7, !MALUtilities.isEmpty(result.getDealerName()) ? result.getDealerName() : null );
        ps.setString(8, !MALUtilities.isEmpty(result.getDealerPhone()) ? result.getDealerPhone() : null );
        ps.setString(9, !MALUtilities.isEmpty(result.getAddress()) ? result.getAddress() : null );
        ps.setString(10, !MALUtilities.isEmpty(result.getCity()) ? result.getCity() : null );
        ps.setString(11, !MALUtilities.isEmpty(result.getState()) ? result.getState() : null );
        ps.setString(12, !MALUtilities.isEmpty(result.getZip()) ? result.getZip() : null );
        ps.setString(13, !MALUtilities.isEmpty(result.getFax()) ? result.getFax() : null );
        ps.setString(14, result.getSupplierExistsYN());
        ps.setLong(15, result.getMakeId());
        ps.setString(16, result.getNcvBatchYN());
        ps.setString(17, result.getCvBatchYN());
        ps.setDate(18, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        ps.setDate(19, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        ps.setString(20,  result.getProcessedFileName());
    }
 
}