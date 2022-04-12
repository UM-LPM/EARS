package org.um.feri.ears.statistic.rating_system.glicko2;

public class GameOutcomes {
    int win, lose, draw;

    public GameOutcomes(int win, int lose, int draw) {
        super();
        this.win = win;
        this.lose = lose;
        this.draw = draw;
    }

    public GameOutcomes(GameOutcomes gameOutcomes) {
        this.win = gameOutcomes.win;
        this.lose = gameOutcomes.lose;
        this.draw = gameOutcomes.draw;
    }

    public void incLoss() {
        lose++;
    }

    public void incDraw() {
        draw++;
    }

    public void incWin() {
        win++;
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
