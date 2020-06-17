package engine.core;

import javax.swing.*;

import engine.helper.Assets;
import engine.helper.MarioActions;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;


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
        g2d.drawRect(debugRect.x-1, debugRect.y-1, debugRect.width+2, debugRect.height+2);
        g2d.setPaint(Color.lightGray);

        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        g2d.setComposite(alcom);

        g2d.fillRect(debugRect.x, debugRect.y, debugRect.width, debugRect.height);

        MarioForwardModel model = new MarioForwardModel(world);

        int[][] sceneObservation = model.getMarioSceneObservation(2);
        int[] marioPosition = model.getMarioScreenTilePos();

        for(int i = 0; i < sceneObservation.length; i++){
            for(int j = 0; j< sceneObservation[0].length; j++){
                if(sceneObservation[i][j] != 0){
                    drawCube(g2d, debugRect, i, j);
                }
            }
        }
        drawMario(g2d, debugRect, model.getMarioMode());
    }
    private void drawCube(Graphics g, Rectangle rect, int x, int y){
        int planeWidth = rect.width/16;
        int planeHeight = rect.height/16;
        int planeX = rect.x + x * planeWidth;
        int planeY = rect.y + y * planeHeight;

        Graphics2D g2 = (Graphics2D) g;

        Rectangle2D plane = new Rectangle2D.Double(planeX, planeY, planeWidth, planeHeight);
        Rectangle2D inner = new Rectangle2D.Double(planeX+1, planeY+1, planeWidth-2, planeHeight-2);
        g2.setColor(Color.BLACK);
        g2.draw(plane);
        g2.fill(plane);

        g2.setColor(Color.white);
        g2.draw(inner);
        g2.fill(inner);
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