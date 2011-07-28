package mw;


public class MagicSearcherMtgImpl implements MagicSearcherIf {

	private MagicSearcherCost magicSrchCost; 
	private MagicSearcherImage magicSrchImg;
	
	public MagicSearcherMtgImpl(){
		magicSrchCost = new MagicSearcherCost(4.2,1,false,false);
		magicSrchImg = new MagicSearcherImage();
	}
	
	@Override
	public void getPrices() {
		magicSrchCost.getPrices();
	}

	@Override
	public void getImages() {
		magicSrchImg.getImages();
	}

	 
	

	
	
}
