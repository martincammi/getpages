package mw.htmlParserMC;

	public class ParserException extends Exception {

		private static final long serialVersionUID = 1L;

		public ParserException() {
		}
	
		public ParserException(String message) {
			super(message);
		}
	
		public ParserException(Throwable throwable) {
			super(throwable);
		}
	
		public ParserException(String message, Throwable throwable) {
			super(message, throwable);
		}
	}