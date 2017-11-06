package abideFinancial;

class PostCodeRank implements Comparable<PostCodeRank>{
	private String postCode;
	private Double totalSpend;
	
	PostCodeRank(){
		postCode = null;
		totalSpend = 0.0;
	}
	
	PostCodeRank(String _postCode, Double _totalSpend){
		postCode = _postCode;
		totalSpend = _totalSpend;
	}
	
	@Override
	public int compareTo(PostCodeRank o) {
        return totalSpend.compareTo(o.totalSpend);
    }
	
	public String getPostCode(){
		return postCode;
	}
	
	public Double getTotalSpend(){
		return totalSpend;
	}
	
	public void setPostCode(String _postCode){
		postCode = _postCode;
	}
	
	public void setTotalSpend(Double _totalSpend){
		totalSpend = _totalSpend;
	}
}