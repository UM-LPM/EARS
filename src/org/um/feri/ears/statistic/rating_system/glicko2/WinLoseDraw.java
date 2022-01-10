package org.um.feri.ears.statistic.rating_system.glicko2;

public class WinLoseDraw {
    int win, lose, draw;

    public WinLoseDraw(int win, int lose, int draw) {
        super();
        this.win = win;
        this.lose = lose;
        this.draw = draw;
    }
    public WinLoseDraw(WinLoseDraw wld) {
    	this.win = wld.win;
        this.lose = wld.lose;
        this.draw = wld.draw;
	}
	public void incLoss() {
        this.lose++;
    }
    public void incDraw() {
        this.draw++;
    }
    public void incWin() {
        this.win++;
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

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
    @Override
    public String toString() {
        return "[win=" + win + ", lose=" + lose + ", draw=" + draw + "]";
    }
    
    
}
