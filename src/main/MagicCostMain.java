package main;

import mw.MagicSearcherIf;
import mw.MagicSearcherMtgImpl;

/**
 * Do search over Magic Cards
 * @author Martin Cammi
 *
 */

public class MagicCostMain {
	 
	 public static void main(String[] args) throws Exception{
		 MagicSearcherIf magicSearcher = new MagicSearcherMtgImpl();
		 magicSearcher.getPrices();
	 }
}
