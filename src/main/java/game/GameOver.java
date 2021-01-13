package game;

import graphics.Paths;
import minMax.GeneticAlgorithm.GA;
import minMax.MinMax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GameOver {
    File winR = new File("winR.txt");
    File drawR = new File("drawR.txt");
    // JFrame for the current game, so it can close it down
    private JFrame frame;
    // game over screen.
    private JFrame next;
    private JPanel panel;
    static int drawCounter=0;
    static int gamesPlayed=0;
    double winRaten;
    double drawRate;

    public GameOver(JFrame frame){
        this.frame=frame;
    }
    // turns it on
    public void toggle() throws IOException, InterruptedException {
        gamesPlayed++;
        if (Graph.GASim) {
            if (Graph.getPlayer2Score() == Graph.getPlayer1Score()) {
                GA.draws++;
            }else {
                if (((Graph.getPlayer1Score() > Graph.getPlayer2Score()) && Graph.isMiniMaxP1()) || (Graph.getPlayer1Score() < Graph.getPlayer2Score()) && !Graph.isMiniMaxP1()) {
                    GA.wins++;
                } else {
                    GA.loses++;
                }
            }
            System.out.println(gamesPlayed+": "+GA.wins+":"+GA.draws+":"+GA.loses);
            if (gamesPlayed < Graph.sims) {
                Graph.player1Score = 0;
                Graph.player2Score = 0;
                if(gamesPlayed>(Graph.sims/2)){
                    Graph.setMiniMaxP1(false);
                    Graph.setRandBotPlayer1(true);
                }
                new GameBoard();
            } else {
                GA.finished=true;
                gamesPlayed=0;
                Graph.player1Score = 0;
                Graph.player2Score = 0;
                double score = GA.wins+((double)(GA.draws)*0.25);
                double[] toget = new double[6];
                toget[0]=GA.tempA;
                toget[1]=GA.tempB;
                toget[2]=GA.tempC;
                toget[3]=GA.tempD;
                toget[4]=GA.tempE;
                toget[5]=score;
                GA.wins=0;
                GA.draws=0;
                GA.loses=0;
                GA.values.add(toget);
                GA.createBots();
                if(GA.number==0){
                    GA.sortBots();
                }
            }
        } else {
            if (Graph.allWaysReplay) {
                if (Graph.gamesToPlay == 0) {
                    System.out.println("SCORE: " + Graph.getPlayer1Score() + " : " + Graph.getPlayer2Score());
                    if (Graph.getPlayer1Score() > Graph.getPlayer2Score()) {
                        Graph.setGamesWon1(Graph.getGamesWon1() + 1);
                    } else {
                        if (Graph.getPlayer2Score() > Graph.getPlayer1Score()) {
                            Graph.setGamesWon2(Graph.getGamesWon2() + 1);
                        }
                        if (Graph.getPlayer2Score() == Graph.getPlayer1Score()) {
                            drawCounter++;
                        }
                    }
                    System.out.println("1: " + Graph.getGamesWon1() + " 2: " + Graph.getGamesWon2());
                    winRaten = 100.0 * ((double) (Graph.getGamesWon2()) / (double) (gamesPlayed));
                    drawRate = 100.0 * ((double) (drawCounter) / (double) (gamesPlayed));
                    FileWriter writer = new FileWriter("winR.txt", true);
                    writer.write(Double.toString(winRaten) + '\n');
                    writer.close();
                    FileWriter writer1 = new FileWriter("drawR.txt");
                    writer1.write(Double.toString(drawRate) + '\n');
                    if (Graph.getGamesWon2() != 0) {
                        System.out.println("WIN PERCENTAGE: " + winRaten + " | DRAW PERCENTAGE: " + drawRate);
                    }
                    Graph.player1Score = 0;
                    Graph.player2Score = 0;
                    new GameBoard();
                } else {
                    System.out.println(gamesPlayed);
                    if (Graph.getPlayer1Score() > Graph.getPlayer2Score()) {
                        Graph.setGamesWon1(Graph.getGamesWon1() + 1);
                    } else {
                        if (Graph.getPlayer2Score() > Graph.getPlayer1Score()) {
                            Graph.setGamesWon2(Graph.getGamesWon2() + 1);
                        }
                        if (Graph.getPlayer2Score() == Graph.getPlayer1Score()) {
                            drawCounter++;
                        }
                    }
                    if (gamesPlayed < Graph.gamesToPlay) {
                        Graph.player1Score = 0;
                        Graph.player2Score = 0;
                        new GameBoard();
                    } else {
                        System.out.println("OVERALL SCORE: " + Graph.getGamesWon1() + ":" + Graph.getGamesWon2());
                        System.out.println("DRAWS: " + (Graph.gamesToPlay - (Graph.getGamesWon1() + Graph.getGamesWon2())));
                    }
                }
            } else {
                frame.setVisible(false);
                next = new JFrame();
                panel = new JPanel(new FlowLayout());
                panel.setBounds(0, 0, Paths.FRAME_WIDTH, Paths.FRAME_HEIGHT);
                panel.setBackground(Color.DARK_GRAY);
                panel.setOpaque(true);
                JLabel text = new JLabel();
                text.setText("GAME OVER");
                text.setFont(new Font("TimesRoman", Font.BOLD, (int) (Math.sqrt(Paths.FRAME_HEIGHT * Paths.FRAME_WIDTH) / 8)));
                JLabel otherText = new JLabel();
                otherText.setFont(new Font("TimesRoman", Font.PLAIN, (int) (Math.sqrt(Paths.FRAME_HEIGHT * Paths.FRAME_WIDTH) / 10)));
                JLabel wonCounter = new JLabel();
                wonCounter.setFont(new Font("TimesRoman", Font.PLAIN, (int) (Math.sqrt(Paths.FRAME_HEIGHT * Paths.FRAME_WIDTH) / 20)));
                if (Graph.getPlayer1Score() > Graph.getPlayer2Score()) {
                    text.setForeground(Color.RED);
                    otherText.setForeground(Color.RED);
                    otherText.setText("Player 1 wins");
                    Graph.setGamesWon1(Graph.getGamesWon1() + 1);
                    wonCounter.setForeground(Color.RED);
                    if (Graph.getGamesWon1() > 1) {
                        wonCounter.setText("They have won " + Graph.getGamesWon1() + " times. " + '\n');
                    } else {
                        wonCounter.setText("They have won " + Graph.getGamesWon1() + " time. " + '\n');
                    }
                } else {
                    if (Graph.getPlayer2Score() > Graph.getPlayer1Score()) {
                        text.setForeground(Color.BLUE);
                        otherText.setForeground(Color.BLUE);
                        otherText.setText("Player 2 wins");
                        Graph.setGamesWon2(Graph.getGamesWon2() + 1);
                        wonCounter.setForeground(Color.BLUE);
                        if (Graph.getGamesWon2() > 1) {
                            wonCounter.setText("They have won " + Graph.getGamesWon2() + " times. " + '\n');
                        } else {
                            wonCounter.setText("They have won " + Graph.getGamesWon2() + " time. " + '\n');
                        }
                    } else {
                        text.setForeground(Color.WHITE);
                        otherText.setForeground(Color.WHITE);
                        otherText.setText("TIE");
                    }
                }
                JButton playAgain = new JButton("Play again?");
                // creates the next game
                playAgain.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        next.setVisible(false);
                        try {
                            new GameBoard(Graph.getHeight(), Graph.getWidth());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
                JLabel score = new JLabel();
                score.setFont(new Font("TimesRoman", Font.PLAIN, (int) (Math.sqrt(Paths.FRAME_HEIGHT * Paths.FRAME_WIDTH) / 20)));
                score.setForeground(Color.WHITE);
                score.setText("The score was " + Graph.getPlayer1Score() + " : " + Graph.getPlayer2Score());
                panel.add(text);
                panel.add(otherText);
                panel.add(wonCounter);
                panel.add(playAgain);
                panel.add(score);
                next.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                next.setSize(Paths.FRAME_WIDTH, Paths.FRAME_HEIGHT);
                next.setResizable(false);
                next.add(panel);
                next.setVisible(true);
            }
        }
    }
}
