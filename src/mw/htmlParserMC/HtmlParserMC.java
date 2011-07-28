package mw.htmlParserMC;

/**
 * @author Martin Cammi
 * Html Parser with state.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParserMC {
	
	protected String ATTRIBUTES = "ATTRIBUTES"; 
	
	protected String TABLE_PATTERN = "<table\\s*[^>]*>(.*?)</table>";
	protected String TABLE_PATTERN_WITH_ATTRIBUTES = "<table\\s*.*?"+ ATTRIBUTES +"[^>]*>(.*?)</table>";
	protected String TABLE_MAGIC = "<table\\s*width=\"100%\"\\s*border=\"0\"\\s*cellpadding=\"3\"\\s*cellspacing=\"1\"\\s*class=\"resultList\">(.*?)\\s+</table>";
	
	//protected String ROW_PATTERN = "<tr\\s*[^>]*>(.*?)</tr>";
	protected String ROW_PATTERN = "</tr>\\s+[^>]*<tr>(.*?)</tr>";
	protected String ROW_MAGIC = "<tr\\s*class=\"darkRow\">(.*?)</tr>";
	protected String COLUMN_PATTERN = "<td\\s*[^>]*>(.*?)</td>";
	//protected String VALUE_PATTERN = "<[^>]*>(.*?)</[^>]*>";
	protected String PRICE_PATTERN = "<span\\s*[^>]*>(.*?)</span>";
	protected String OPTION_PATTERN = "<option\\s*[^>]*>(.*?)</option>";
	
	public static String NO_QUANTITY = "-";
	
	//Table Pattern
	private Pattern tablePattern;
	private Matcher tableMatcher;
	
	//Row Pattern
	private Pattern rowPattern;
	private Matcher rowMatcherEven;
	private Matcher rowMatcherOdd;
	
	//Column Pattern
	private Pattern columnPattern;
	private Matcher columnMatcher;
	
	private String originalText;
	private String text;
	private String tableText;
	private String rowText;
	private String columnText;
	
	private int rowCount = 0;
	
	public HtmlParserMC(String textToParse){
		originalText = textToParse;
		text = textToParse;
	}
	
	public void resetText(){
		text = originalText;
	}
	
	/**
	 * Parse the first occurrence of a table
	 */
	public void parseTable() throws ParserException {
		tablePattern = Pattern.compile(TABLE_PATTERN,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		tableMatcher = tablePattern.matcher(text);
	}

	/**
	 * Skips the next table 
	 */
	public void skipTable(){
		if(tableMatcher.find()){
			String deleteVaribel = tableMatcher.group(1);
			deleteVaribel = deleteVaribel + "";
		}
	}
	
	/**
	 * Gets the next table 
	 * @return
	 */
	public String nextTable(){

		if(tableMatcher.find()){
			tableText = tableMatcher.group(1);
			return tableText;
			
		}
		
		return null;
	}
	/**
	 * Gets the Xth table where X begin in 0
	 * @param tableIndex
	 * @return
	 */
	public String nextTable(int tableIndex){
		
		for (int i = 0; tableIndex > i; i++) {
			skipTable();
		}
		tableText = nextTable();
		return tableText; 
	}

	
	/**
	 * Parse the first occurrence of a table with an attribute and a value as first attribute of the table.
	 */
	@Deprecated
	public void parseTableWithAttribute(String attribute, String value) throws ParserException {
		String attributes = attribute + "=" + "\\\"" + value + "\\\"";
		String newTablePattern = TABLE_PATTERN_WITH_ATTRIBUTES.replace(ATTRIBUTES, attributes);
		tablePattern = Pattern.compile(newTablePattern,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		tableMatcher = tablePattern.matcher(text);
		
		if(tableMatcher.find()){
			tableText = tableMatcher.group(0);
		}else{
			throw new ParserException("Table not found");
		}
	}
	
	public void parseMagicTable() throws ParserException{
		tablePattern = Pattern.compile(TABLE_MAGIC,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		tableMatcher = tablePattern.matcher(text);
		
		if(tableMatcher.find()){
			tableText = tableMatcher.group(0);
		}else{
			throw new ParserException("Table not found");
		}
	}
	
	/**
	 * Parse the first occurrence of a row
	 */
	public void parseRow() throws ParserException {
		
		//if(rowCount %2 == 0){
			rowPattern = Pattern.compile(ROW_MAGIC,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			rowMatcherEven = rowPattern.matcher(tableText);
		//}else{
			rowPattern = Pattern.compile(ROW_PATTERN,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		//}
			rowMatcherOdd = rowPattern.matcher(tableText);
		//rowCount++;
		
	}
	
	/**
	 * Skips the next row 
	 */
	public void skipRow(){
		if(rowCount %2 == 0){
			if(rowMatcherEven.find()){
				rowCount++;
				rowMatcherEven.group(1);
			}
		}else{
			if(rowMatcherOdd.find()){
				rowCount++;
				rowMatcherOdd.group(1);
			}
		}
	}
	
	/**
	 * Gets the next row 
	 * @return
	 */
	public String nextRow(){

		if(rowCount %2 == 0){
			if(rowMatcherEven.find()){
				rowText = rowMatcherEven.group(1);
				rowCount++;
				return rowText;
				
			}
		}else{
			if(rowMatcherOdd.find()){
				rowText = rowMatcherOdd.group(1);
				rowCount++;
				return rowText;
			}
		}
		
		return null;
	}
	/**
	 * Gets the Xth row where X begin in 0
	 * @param rowIndex
	 * @return
	 */
	public String nextRow(int rowIndex){
		
		for (int i = 0; rowIndex > i; i++) {
			skipRow();
		}
		rowText = nextRow();
		return rowText; 
	}
	
	/**
	 * Gets the first occurrence of a column
	 */
	public void parseColumn() throws ParserException {
		columnPattern = Pattern.compile(COLUMN_PATTERN,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		columnMatcher = columnPattern.matcher(rowText);
	}

	/**
	 * Skips the next column 
	 */
	public void skipColumn(){
		if(columnMatcher.find()){
			columnMatcher.group(1);
		}
	}
	
	/**
	 * Gets the next column 
	 * @return
	 */
	public String nextColumn(){

		if(columnMatcher.find()){
			columnText = columnMatcher.group(1);
			return columnText;
		}
		return null;
	}
	/**
	 * Gets the Xth column where X begin in 0
	 * @param columnIndex
	 * @return
	 */
	public String nextColumn(int columnIndex){
		
		for (int i = 0; columnIndex > i; i++) {
			skipColumn();
		}
		columnText = nextColumn();
		return columnText; 
	}
	
	public String extractPrice(){
		Pattern valuePattern = Pattern.compile(PRICE_PATTERN,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher valueMatcher = valuePattern.matcher(columnText);
		
		if(valueMatcher.find()){
			return valueMatcher.group(1);
		}
		
		return null;
	}
	
	public String extractQuantity(){
		if(NO_QUANTITY.equals(columnText.trim())){
			return NO_QUANTITY;
		}
		Pattern optionPattern = Pattern.compile(OPTION_PATTERN,Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher optionMatcher = optionPattern.matcher(columnText);
		
		String lastResult = null;
		while(optionMatcher.find()){
			lastResult = optionMatcher.group(1);
		}
		
		return lastResult;
	}
}
