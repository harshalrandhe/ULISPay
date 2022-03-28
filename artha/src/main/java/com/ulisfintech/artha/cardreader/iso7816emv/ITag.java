package com.ulisfintech.artha.cardreader.iso7816emv;

import com.ulisfintech.artha.cardreader.enums.TagTypeEnum;
import com.ulisfintech.artha.cardreader.enums.TagValueTypeEnum;

public interface ITag {

	enum Class {
		UNIVERSAL, APPLICATION, CONTEXT_SPECIFIC, PRIVATE
	}

	boolean isConstructed();

	byte[] getTagBytes();

	String getName();

	String getDescription();

	TagTypeEnum getType();

	TagValueTypeEnum getTagValueType();

	Class getTagClass();

	int getNumTagBytes();

}
