package com.model2.mvc.common;


//==>����Ʈȭ���� �𵨸�(�߻�ȭ/ĸ��ȭ)�� Bean 
public class Search {
	
	///Field
	private Integer currentPage;
	private String searchKeyword;
	private Integer pageSize;
	// for ȸ��
	private String searchCondition;  // �˻� Ÿ��
	// for ��ǰ
	private String priceDESC;  // "1"=���ݳ�����, "0"=���ݳ�����, null=��Ȱ��ȭ
	private Integer priceMin;  // ������
	private Integer priceMax;  // �ְ�
	
	//==> ����Ʈȭ�� currentPage�� �ش��ϴ� ȸ�������� ROWNUM ��� SELECT ���� �߰��� Field 
	//==> UserMapper.xml �� 
	//==> <select  id="getUserList"  parameterType="search"	resultMap="userSelectMap">
	//==> ����
	private Integer endRowNum;
	private Integer startRowNum;
	
	///Constructor
	public Search() {
	}
	
	///Method
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer paseSize) {
		this.pageSize = paseSize;
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	
	public String getPriceDESC() {
		return priceDESC;
	}

	public void setPriceDESC(String priceDESC) {
		this.priceDESC = priceDESC;
	}

	public Integer getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(Integer priceMin) {
		this.priceMin = priceMin;
	}

	public Integer getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Integer priceMax) {
		this.priceMax = priceMax;
	}

	/// getter�� �����Ͽ� get���� �����ų �� �ִ�!
	//==> Select Query �� ROWNUM ������ �� 
	public Integer getEndRowNum() {
		System.out.println("getEndRowNum()");    
		return getCurrentPage()*getPageSize();
	}
	//==> Select Query �� ROWNUM ���� ��
	public Integer getStartRowNum() {
		System.out.println("getStartRowNum()");
		return (getCurrentPage()-1)*getPageSize()+1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Search [currentPage=");
		builder.append(currentPage);
		builder.append(", searchKeyword=");
		builder.append(searchKeyword);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", searchCondition=");
		builder.append(searchCondition);
		builder.append(", priceDESC=");
		builder.append(priceDESC);
		builder.append(", priceMin=");
		builder.append(priceMin);
		builder.append(", priceMax=");
		builder.append(priceMax);
		builder.append(", endRowNum=");
		builder.append(endRowNum);
		builder.append(", startRowNum=");
		builder.append(startRowNum);
		builder.append("]");
		return builder.toString();
	}
}