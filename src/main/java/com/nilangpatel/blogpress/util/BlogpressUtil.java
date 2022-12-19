package com.nilangpatel.blogpress.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogpressUtil {

	private static Logger logger =  LoggerFactory.getLogger(BlogpressUtil.class);
	
	private static String elasticDateFormat = "MM-dd-yyyy'T'HH:mm:ss";
	private static String displayDateFormat = "MMMMM dd yyyy h:mm:ss a";
	private static final String ALPHA_NUMERIC_STRING = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
	private static int randomNoLength=10;
	
	private BlogpressUtil() {
		
	}
	
	/**
	 * This method will format the given date in given date format
	 * @param inputDate
	 * @param dtFormat
	 * @return
	 */
	private static String getFormattedDate(Date inputDate, DateFormat dtFormat) {
		if(inputDate !=null) {
			return dtFormat.format(inputDate);
		}else {
			return "";
		}
	}
	
	/**
	 * This method will return DateFormat object from date pattern.
	 * @param dateFormatPattern
	 * @return
	 */
	public static DateFormat getDateFormatObj(String dateFormatPattern) {
		DateFormat dtFormat = new SimpleDateFormat(dateFormatPattern);
		return dtFormat;
	}
	
	/**
	 * This method will return formatted Date for elastic search
	 * @param inputDate
	 * @return
	 */
	public static String getFormattedDateForSearch(Date inputDate) {
		return getFormattedDate(inputDate, getDateFormatObj(elasticDateFormat));
	}
	
	
	/**
	 * This method will return formatted Date for display on web page
	 * @param inputDate
	 * @return
	 */
	public static String getFormattedDateForDisplayOnPage(Date inputDate) {
		return getFormattedDate(inputDate, getDateFormatObj(displayDateFormat));
	}
	
	/**
	 * This method generates random number with long value of date object
	 *
	 * @param currentDate
	 * @return random number
	 */
	public static Long RandonmNumber(Date currentDate) {
		int count = randomNoLength;
		StringBuilder builder = new StringBuilder();

		Date date = currentDate;
		if(date ==null) {
			currentDate = new Date();
		}
		return  Long.parseLong(String.valueOf(builder.append(currentDate.getTime())));
	}
	
	public static int parseInteger(String intStr) {
		int returnValue=0;
		try {
			returnValue = Integer.parseInt(intStr);
		}catch(NumberFormatException e) {
			logger.error("error occuired while parsing integer "+intStr,e.getMessage(),e);
		}
		return returnValue;
	}
	
/*	*//**
	 * This method update comment from list for given comment id and send the updated list back.
	 * @param arrayList
	 * @param updateCommentId
	 * @param updatedStatus
	 * @return
	 *//*
	public static List<Comment> updateCommentStatus(List<Comment> arrayList,String commentId,String updatedStatus) {
		List<Comment> updatedCommentList = null;
		if(arrayList !=null) {
			ObjectMapper objectMapper = new ObjectMapper();
		    //objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			try {
				//arrayToJson = objectMapper.writeValueAsString(arrayList);
				
				JSONArray commentListArr = new JSONArray(arrayList);
				JSONObject commentJson = null;
				for(int index=0;index < commentListArr.length();index++) {
					commentJson = commentListArr.getJSONObject(index);
					if(commentJson !=null) {
						if( commentId.equals((String)commentJson.get("id"))) {
							commentJson.put("status", updatedStatus);
							break;
						}
					}
				}
				updatedCommentList = objectMapper.readValue(commentListArr.toString(), 
						TypeFactory.defaultInstance().constructCollectionType (List.class, Comment.class));				
				
			} catch (JsonProcessingException e) {
				logger.error("error occuired while parsing comment list to json array "+e.getMessage(),e);
			} catch (IOException e) {
				logger.error("error occuired while parsing comment list to json array "+e.getMessage(),e);
			}
		}
		return updatedCommentList;
	}*/
	
}
