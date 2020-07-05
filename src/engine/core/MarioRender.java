package engine.core;

import javax.swing.*;

import engine.helper.Assets;
import engine.helper.MarioActions;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MarioRender extends JComponent implements FocusListener {
    private static final long serialVersionUID = 790878775993203817L;
    public static final int TICKS_PER_SECOND = 24;

    private float scale;
    private GraphicsConfiguration graphicsConfiguration;

    int frame;
    Thread animator;
    boolean focused;

    public MarioRender(float scale) {
        this.setFocusable(true);
        this.setEnabled(true);
        this.scale = scale;

        Dimension size = new Dimension((int) (256 * scale), (int) (240 * scale));

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setFocusable(true);
    }

    public void init() {
        graphicsConfiguration = getGraphicsConfiguration();
        Assets.init(graphicsConfiguration);
    }

    public void renderWorld(MarioWorld world, Image image, Graphics g, Graphics og) {
        og.fillRect(0, 0, 256, 240);
        world.render(og);
        drawStringDropShadow(og, "Lives: " + world.lives, 0, 0, 7);
        drawStringDropShadow(og, "Coins: " + world.coins, 11, 0, 7);
        drawStringDropShadow(og, "Time: " + (world.currentTimer == -1 ? "Inf" : (int) Math.ceil(world.currentTimer / 1000f)), 22, 0, 7);
        if (MarioGame.verbose) {
            debugView(world, og);
            String pressedButtons = "";
            for (int i = 0; i < world.mario.actions.length; i++) {
                if (world.mario.actions[i]) {
                    pressedButtons += MarioActions.getAction(i).getString() + " ";
                }
            }
            drawStringDropShadow(og, "Buttons: " + pressedButtons, 0, 2, 1);
        }
        if (scale > 1) {
            g.drawImage(image, 0, 0, (int) (256 * scale), (int) (240 * scale), null);
        } else {
            g.drawImage(image, 0, 0, null);
        }
    }

    public void drawStringDropShadow(Graphics g, String text, int x, int y, int c) {
        drawString(g, text, x * 8 + 5, y * 8 + 5, 0);
        drawString(g, text, x * 8 + 4, y * 8 + 4, c);
    }

    private void drawString(Graphics g, String text, int x, int y, int c) {
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            g.drawImage(Assets.font[ch[i] - 32][c], x + i * 8, y, null);
        }
    }

    public void focusGained(FocusEvent arg0) {
        focused = true;
    }

    public void focusLost(FocusEvent arg0) {
        focused = false;
    }

    private void debugView(MarioWorld world, Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle debugRect = new Rectangle(256/16, 240/4, 256/4, 256/4);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(debugRect.x-1, debugRect.y-1, debugRect.width+2, debugRect.height+2);
        g2d.setPaint(Color.lightGray);

        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        g2d.setComposite(alcom);

        g2d.fillRect(debugRect.x, debugRect.y, debugRect.width, debugRect.height);

        MarioForwardModel model = new MarioForwardModel(world);

        int[][] sceneObservation = model.getMarioCompleteObservation(2,2);
        int[][] sceneVision = carlos.Utils.ArrayUtils.getSubmatrix(sceneObservation,9,6,13,10);

        for(int i = 0; i < sceneVision.length; i++){
            for(int j = 0; j< sceneVision[0].length; j++){
                if(sceneVision[i][j] != 0){
                    drawCube(g2d, debugRect, i + 9, j + 6, Color.WHITE);
                }
            }
        }

        drawBox(g, debugRect, 9, 6, 4, 3, Color.ORANGE);
        drawBox(g, debugRect, 9, 9, 4, 1, Color.MAGENTA);
        drawMario(g2d, debugRect, model.getMarioMode());
    }

    private void drawCube(Graphics g, Rectangle rect, int x, int y,  Color innerColor){
        int planeWidth = rect.width/16;
        int planeHeight = rect.height/16;
        int planeX = rect.x + x * planeWidth;
        int planeY = rect.y + y * planeHeight;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawRect(planeX, planeY, planeWidth, planeHeight);

        g2.setColor(innerColor);
        g2.fillRect(planeX + 1, planeY + 1, planeWidth - 2, planeHeight - 2);
    }

    private void drawBox(Graphics g, Rectangle rect, int x, int y, int width, int height, Color borderColor){
        int planeWidth = width * (rect.width/16);
        int planeHeight = height * (rect.height/16);
        int planeX = rect.x + x * (rect.width/16);
        int planeY = rect.y + y * (rect.height/16);

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(borderColor);
        g2.drawRect(planeX, planeY, planeWidth, planeHeight);
    }

    private void drawMario(Graphics g, Rectangle rect, int mode){
        int height = mode < 1 ? 1 : 2;
        int marioHeight = height * (rect.height/16);
        int marioX = rect.x + rect.width/2 + (rect.width/16)/2;
        int marioY = rect.y + rect.height/2 + (rect.height/16)/2;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.RED);
        g2d.drawLine(marioX, marioY, marioX, marioY-(marioHeight/2));
    }

}