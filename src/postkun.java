// postkun nawatobi


import com.nttdocomo.ui.*;

public class postkun extends IApplication
{
    public void start()
    {
        pCanvas p = new pCanvas();
        p.init(this);
        Display.setCurrent(p);

        p.go();
    }
}

