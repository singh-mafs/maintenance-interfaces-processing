package com.mikealbert.batch.processors;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.mikealbert.data.MatchingListFactory;
import com.mikealbert.data.util.PropertyMatchVO;
import com.mikealbert.util.ObjectUtils;

public class OpCodeDerivingProcessor<T> implements ItemProcessor<T, T> {
	private List<?> matchingList;
	private List<PropertyMatchVO> matchingProperties;
	private MatchingListFactory listFactory;
	//TODO: (if needed) private boolean addOnMissingValue;
		
	@Override
	public T process(T targetItem) throws Exception {
		matchingList = listFactory.getMatchingList();
		//TODO: boolean (Delete?) do we need to handle this somehow?
		//TODO: will this approach be too slow? (it should not there should be only a few hundred entries at most).
		//TODO: use the factory so that we regularly reload the list (maybe with each provider somehow) ** I.E. in multi-threaded we might need to syncronize
		String opCode = "";
			//TODO: if it is required
			if(matchingList.size() > 0){
				for(Object sourceItem : matchingList){
					if(matchingPropertiesFound(sourceItem,targetItem)){
						// it is an "M", break and return
						opCode = "M";
						break;
					}else{
						opCode = "A";
						// set the op code and continue (next object) until we get a match 
					}
				}
			}else{ // if there are no service providers for this SUP_SUP_ID, then we "A" (add)
				opCode = "A";
			}		
		
		//TODO: do we need to support matching "optional properties" to derive the op code?
		//if it is not required
		// 	if it is found in the source and the target
		//		(so far it's an "M")
		//	if it is not found then
		//		if addOnMissingValue = true
		//			it is an "A"
		//		else
		//			is is an "M"
		ObjectUtils.setProperty(targetItem, "operationCode", opCode);
		
		return targetItem;
	}
	
	private boolean matchingPropertiesFound(Object sourceItem, Object targetItem){
		boolean propsMatch = false;
		
		// for each matching property
		for(PropertyMatchVO prop : matchingProperties){
			// if it matches in the source and the target
			if(ObjectUtils.propertyValuesMatch(sourceItem, prop.getSourcePropertyName(), targetItem, prop.getTargetPropertyName(), true, prop.getExcludedCharacters())){
				// if all match then return "true"
				propsMatch = true;
				// continue (next property)
			}else{ // else if it is not found then
				// otherwise return false
				propsMatch = false;
				break;
			}
		}
		
		return propsMatch;
	}


//	public boolean isAddOnMissingValue() {
//		return addOnMissingValue;
//	}
//
//
//	public void setAddOnMissingValue(boolean addOnMissingValue) {
//		this.addOnMissingValue = addOnMissingValue;
//	}


	public List<?> getMatchingList() {
		return matchingList;
	}


	public void setMatchingList(List<?> matchingList) {
		this.matchingList = matchingList;
	}


	public List<PropertyMatchVO> getMatchingProperties() {
		return matchingProperties;
	}


	public void setMatchingProperties(List<PropertyMatchVO> matchingProperties) {
		this.matchingProperties = matchingProperties;
	}
	
	public MatchingListFactory getListFactory() {
		return listFactory;
	}

	public void setListFactory(MatchingListFactory listFactory) {
		this.listFactory = listFactory;
	}





}
