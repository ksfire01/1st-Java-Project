package Model;

public class MemberVO {
	private String txtId;
	private String name;
	private String nick;
	private String txtPass;
	private int win;
	private int lose;
	private int totalGame;
	
	public MemberVO(){
		
	}
	
	
	public MemberVO(String txtId, String name, String nick, String txtPass, int win, int lose, int totalGame) {
		super();
		this.txtId = txtId;
		this.name = name;
		this.nick = nick;
		this.txtPass = txtPass;
		this.win=win;
		this.lose=lose;
		this.totalGame=totalGame;
	}


	public String getTxtId() {
		return txtId;
	}


	public void setTxtId(String txtId) {
		this.txtId = txtId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNick() {
		return nick;
	}


	public void setNick(String nick) {
		this.nick = nick;
	}


	public String getTxtPass() {
		return txtPass;
	}


	public void setTxtPass(String txtPass) {
		this.txtPass = txtPass;
	}


	public int getWin() {
		return win;
	}


	public void setWin(int win) {
		this.win = win;
	}


	public int getLose() {
		return lose;
	}


	public void setLose(int lose) {
		this.lose = lose;
	}


	public int getTotalGame() {
		return totalGame;
	}


	public void setTotalGame(int totalGame) {
		this.totalGame = totalGame;
	}


	@Override
	public String toString() {
		return "MemberVO [txtId=" + txtId + ", name=" + name + ", nick=" + nick + ", txtPass=" + txtPass + ", win="
				+ win + ", lose=" + lose + ", totalGame=" + totalGame + "]";
	}
	
	
	
}
