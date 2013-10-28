
import com.nttdocomo.ui.*;

public class pCanvas extends Canvas implements Runnable {

    int stat;
    int cnt;
    int jump;
    int kai;
    postkun po;
    int x, y;
	
    Image post[] = new Image[2];
//    AudioPresenter ap = AudioPresenter.getAudioPresenter();

    // drawPolyline の座標とその線の数
    static int nawax[] = {
        19, 25, 38, 46, 42, 41, 14,
        19, 31, 42, 42, 32, 01, -3, 3, 14,
        19, 30, 35, 22, 02, 07, 14,
        19, 29, 31, 12, 00, 03, 14,
        19, 27, 11, -7, -7, 07, 14,
        19, 16, 05, -5, -5, 07, 14,
        19, 23, 22, 18, 11, 14 };
    static int naway[] = {
        46, 54, 58, 58, 54, 53, 46,
        46, 50, 42, 28, 20, 18, 29, 42, 46,
        46, 41, 15, -5, 10, 44, 48,
        46, 46, 22, 06, 28, 42, 46,
        46, 42, 23, 20, 28, 44, 46,
        46, 54, 58, 55, 51, 47, 46,
        41, 50, 55, 56, 52, 53 };
    static int nawac[] = { 7, 9, 7, 7, 7, 7, 6 };

    Graphics g;

    public pCanvas() {
    }

    public void init(postkun po) {
        MediaImage mi;
        MediaSound ms;
//        setSoftLabel(Frame.SOFT_KEY_1, "JUMP");
        setSoftLabel(Frame.SOFT_KEY_1, "EXIT");
        setBackground(Graphics.getColorOfName(Graphics.WHITE));
        g = this.getGraphics();

        this.po = po;

        try {
            mi = MediaManager.getImage("resource:///post.gif");
            mi.use();
            post[0] = mi.getImage();
            mi = MediaManager.getImage("resource:///post2.gif");
            mi.use();
            post[1] = mi.getImage();
//            ms = MediaManager.getSound("resource:///ouch.mid");
//            ms.use();
//            ap.setSound(ms);
        }
        catch (Exception e) {
        }

        x = (this.getWidth() - post[0].getWidth()) / 2;
        y = (this.getHeight() - post[0].getHeight()) / 2;
        stat = 0;
        cnt = 0;
        jump = 0;
    }
    public void go() {
        Thread runner = new Thread(this);
        runner.start();
    }
	
    public void run() {
        for (;;) {
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {
            }

            repaint();
            handleKeyState();

            switch (stat) {
            case 0:		// initTitle
                setSoftLabel(Frame.SOFT_KEY_2, "PLAY");
                cnt = 0;
                jump = 0;
                stat = 1;
                break;
            case 1:		// title
                cnt++;	// handleKeyState() で直接statを書き換える予定
                if (cnt == 5) {
                    jump = 6;
                }
                if (cnt > 6) {
                    cnt = 0;
                }
                break;
            case 2:		// wait ....
                setSoftLabel(Frame.SOFT_KEY_2, "JUMP");
                cnt = 0;
                jump = 0;
                stat = 3;
                break;
            case 3:		// 3...
                try { Thread.sleep(800); } catch (Exception e) {}
                stat = 4;
                break;
            case 4:		// 2...
                try { Thread.sleep(800); } catch (Exception e) {}
                stat = 5;
                break;
            case 5:		// 1...
                try { Thread.sleep(800); } catch (Exception e) {}
                stat = 6;
                break;
            case 6:		// start!
                try { Thread.sleep(800); } catch (Exception e) {}
                stat = 7;
                kai = 0;
                break;
            case 7:
                cnt++;
                if (cnt > 6) {
                    cnt = 0;
                }
                if (cnt == 6) {
                    if (jump != 0) {
                        kai++;
                    }
                    else {
                        stat = 8;
//                        ap.play();
                    }
                }

                break;
            case 8:
                stat = 9;
                setSoftLabel(Frame.SOFT_KEY_2, "REP");
                break;
            case 9:
                try { Thread.sleep(1500); } catch (Exception e) {}
                stat = 10;
                break;
            case 10:
                break;
            }
            
        }
    }

    public void paint(Graphics g)
    {
        g.lock();
        int lx[] = new int[nawac[cnt]];
        int ly[] = new int[nawac[cnt]];
        int c = 0;
        for (int i=0; i<cnt; i++ ) {
            c += nawac[i];
        }
        for (int i=0; i<nawac[cnt]; i++ ) {
            lx[i] = nawax[c+i]+x;
            ly[i] = naway[c+i]+y - jump;
        }

        g.setColor(g.getColorOfName(Graphics.WHITE));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(g.getColorOfName(Graphics.BLACK));
        switch (stat) {
        case 0:
        case 1:
            g.drawString("ポスト君なわとび", 00, this.getHeight() - 5);
            /* FALLTHROU */
        case 7:
            
            if (cnt < 3) {
                g.drawPolyline(lx, ly, nawac[cnt]);
                g.drawImage(post[0], x, y - jump);
            }
            else {
                g.drawImage(post[0], x, y - jump);
                g.drawPolyline(lx, ly, nawac[cnt]);
            }
            if (stat == 7) {
                g.drawString(kai+"回", 0, 15);
            }
            break;
            
        case 2:
            g.drawImage(post[0], x, y);
            break;
        case 3:
        case 4:
        case 5:
        case 6:
            for (int i=0; i<7; i++ ) {
                lx[i] = nawax[i]+x;
                ly[i] = naway[i]+y;
            }
            g.drawPolyline(lx, ly, 7);
            g.drawImage(post[0], x, y);
            if (stat != 6) {
                g.drawString(6-stat+"...", 0, 15);
            }
            else {
                g.drawString("START!", 0, 15);
            }
            break;
        case 8:
        case 9:
        case 10:
            g.drawString(kai+"回", 0, 15);
            g.drawImage(post[1], x - 10, y);
            if (stat == 10) {
                g.drawString("REPLAY?", (this.getWidth() - 70) / 2, this.getHeight() - 5);
            }
            break;
        }
        g.unlock(true);
    }
    
    private void handleKeyState()
    {
        int key = this.getKeypadState();
        if ((key&(1<<Display.KEY_SOFT1)) != 0) {
            po.terminate();
        }
	
        switch (stat) {
        case 1:
            if ((key&(1<<Display.KEY_SOFT2)) != 0) {
                stat = 2;
            }
            if (jump != 0) {
                jump -= 3;
            }
            break;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
            break;
        case 7:
            if (jump == 0) {
                if ((key&(1<<Display.KEY_SOFT2)) != 0 || (key&(1<<Display.KEY_SELECT)) != 0) {
                    jump = 6;
                }
                else {
                    jump = 0;
                }
            }
            else {
                jump-=3;
            }
            break;
        case 8:
        case 9:
            break;
        case 10:
            if ((key&(1<<Display.KEY_SOFT2)) != 0) {
                stat = 2;
            }
            break;
        }
    }
}

