package org.um.feri.ears.rating;

public class WinLossDraw {
    int win, loss, draw;

    public WinLossDraw(int win, int loss, int draw) {
        super();
        this.win = win;
        this.loss = loss;
        this.draw = draw;
    }
    public void incLoss() {
        this.loss++;
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

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
    @Override
    public String toString() {
        return "[win=" + win + ", loss=" + loss + ", draw=" + draw + "]";
    }
    
    
}
