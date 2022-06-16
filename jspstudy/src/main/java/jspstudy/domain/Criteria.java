package jspstudy.domain;

public class Criteria {
	
	private int page ; 			// ������
	private int perPageNum;		// ȭ�鿡 ����Ʈ ��� ����
	
	

	public Criteria() {
		
		this.page = 1;
		this.perPageNum = 15;  		// ������ ���� ������ �ٲٷ��� 15 ���ڸ� �����ϸ� �ȴ�.
		
	}
	
	
	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		if(page <=1) {
			this.page = 1;
			return;
		}
		
		this.page = page;
	}


	public int getPerPageNum() {
		return perPageNum;
	}


	public void setPerPageNum(int perPageNum) {
		if (perPageNum <= 0 || perPageNum > 100) {
			this.perPageNum = 10;
			return;
		}
		
		
		this.perPageNum = perPageNum;
	}



}













