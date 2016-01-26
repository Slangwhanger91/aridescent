package experiment;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import java.util.HashMap;

import static gameGL.util.debug;
import static gameGL.util.debug2;
import static gameGL.util.debug3;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Test3D extends Thread {
    HashMap<String, Float> config = new HashMap<>();
    Float gluPerspective_fovy = 45f;
    Float gluPerspective_aspect = 800f/600f;
    Float gluPerspective_zNear = 0.1f;
    Float gluPerspective_zFar = 20f;
    Float glRotatef_angle = 0f;
    Float glRotatef_angley = 0f;
    Float glRotatef_x = 0f;
    Float glRotatef_y = 1f;
    Float glRotatef_z = 0f;
    Float glTranslatef_x = 0f;
    Float glTranslatef_y = 0f;
    Float glTranslatef_z = 0f;
    Float gluLookAt_eyex = -4f;
    Float gluLookAt_eyey = 4f;
    Float gluLookAt_eyez = 0f;
    Float gluLookAt_centerx = 0f;
    Float gluLookAt_centery = 0f;
    Float gluLookAt_centerz = 0f;
    Float gluLookAt_upx = 0f;
    Float gluLookAt_upy = 1f;
    Float gluLookAt_upz = 0f;

    @Override
    public synchronized void run() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            show();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        System.out.println("Test3D show()");
        initConfig();
        initGL();
        loop();
    }

    void initConfig() {
        config.put("gluPerspective_fovy", gluPerspective_fovy);
        config.put("gluPerspective_aspect", gluPerspective_aspect);
        config.put("gluPerspective_zNear", gluPerspective_zNear);
        config.put("gluPerspective_zFar", gluPerspective_zFar);
        config.put("glRotatef_angle", glRotatef_angle);
        config.put("glRotatef_x", glRotatef_x);
        config.put("glRotatef_y", glRotatef_y);
        config.put("glRotatef_z", glRotatef_z);
        config.put("glTranslatef_x", glTranslatef_x);
        config.put("glTranslatef_y", glTranslatef_y);
        config.put("glTranslatef_z", glTranslatef_z);
        config.put("gluLookAt_eyex", gluLookAt_eyex);
        config.put("gluLookAt_eyey", gluLookAt_eyey);
        config.put("gluLookAt_eyez", gluLookAt_eyez);
        config.put("gluLookAt_centerx", gluLookAt_centerx);
        config.put("gluLookAt_centery", gluLookAt_centery);
        config.put("gluLookAt_centerz", gluLookAt_centerz);
        config.put("gluLookAt_upx", gluLookAt_upx);
        config.put("gluLookAt_upy", gluLookAt_upy);
        config.put("gluLookAt_upz", gluLookAt_upz);
        updateConfig();
    }

    void updateConfig() {
        gluPerspective_fovy = config.get("gluPerspective_fovy");
        gluPerspective_aspect = config.get("gluPerspective_aspect");
        gluPerspective_zNear = config.get("gluPerspective_zNear");
        gluPerspective_zFar = config.get("gluPerspective_zFar");
        glRotatef_angle = config.get("glRotatef_angle");
        glRotatef_x = config.get("glRotatef_x");
        glRotatef_y = config.get("glRotatef_y");
        glRotatef_z = config.get("glRotatef_z");
        glTranslatef_x = config.get("glTranslatef_x");
        glTranslatef_y = config.get("glTranslatef_y");
        glTranslatef_z = config.get("glTranslatef_z");
        gluLookAt_eyex = config.get("gluLookAt_eyex");
        gluLookAt_eyey = config.get("gluLookAt_eyey");
        gluLookAt_eyez = config.get("gluLookAt_eyez");
        gluLookAt_centerx = config.get("gluLookAt_centerx");
        gluLookAt_centery = config.get("gluLookAt_centery");
        gluLookAt_centerz = config.get("gluLookAt_centerz");
        gluLookAt_upx = config.get("gluLookAt_upx");
        gluLookAt_upy = config.get("gluLookAt_upy");
        gluLookAt_upz = config.get("gluLookAt_upz");
    }

    public void exit() {
        Display.destroy();
        System.exit(0);
    }

    void initGL() {
        //glViewport(0, 0, 800, 600);
        //glMatrixMode(GL_PROJECTION);
        //glLoadIdentity();
        //glFrustum(-1f, 1f, -1f, 1f, 1f, 10f);
        //glOrtho(0, 640, 0, 480, 1, -1);

        //glMatrixMode(GL_MODELVIEW);
        //glLoadIdentity();
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glShadeModel(GL_SMOOTH);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        //Keyboard.enableRepeatEvents(true);
    }

    void loop() {
        long tick = 0;

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            draw();
            poll();

            Display.update();
            Display.sync(60);
            debug("tick=%d", tick);
            tick++;
        }
    }

    void draw_test() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //gluPerspective(0f, 800f / 600f, 1f, 11f);
        gluPerspective(gluPerspective_fovy, gluPerspective_aspect, gluPerspective_zNear, gluPerspective_zFar);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt(gluLookAt_eyex, gluLookAt_eyey, gluLookAt_eyez,
                gluLookAt_centerx, gluLookAt_centery, gluLookAt_centerz,
                gluLookAt_upx, gluLookAt_upy, gluLookAt_upz);
        glTranslatef(1f, 1f, -4f);
        glRotatef(glRotatef_angle, glRotatef_x, glRotatef_y, glRotatef_z);

    }

    void draw() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(gluPerspective_fovy, gluPerspective_aspect, gluPerspective_zNear, gluPerspective_zFar);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //glPushMatrix();
        //drawPyramid();
        //glLoadIdentity();
        gluLookAt(gluLookAt_eyex, gluLookAt_eyey, gluLookAt_eyez,
                gluLookAt_centerx, gluLookAt_centery, gluLookAt_centerz,
                gluLookAt_upx, gluLookAt_upy, gluLookAt_upz);
        glRotatef(glRotatef_angle, glRotatef_x, glRotatef_y, glRotatef_z);
        glRotatef(glRotatef_angley, 1f, 0f, 0f);
        drawCubes(10f, 10f);
        //glPopMatrix();

        /*
        glTranslatef(-2f, 0f, -2f);
        glRotatef(89f, 1f, 0f, 0f);
        drawPlane();
        */
    }

    void drawCubes(float xtarget, float ztarget) {
        for (float x = 0; x < xtarget; x += 0.5) {
            glTranslatef(0.5f, 0f, -(ztarget*1f)); // 1f = area of cubes
            for (float z = 0; z < ztarget; z += 0.5) {
                //glTranslatef(glTranslatef_x, glTranslatef_y, glTranslatef_z);
                glTranslatef(0f, 0f, 0.5f);
                drawCube();
            }
        }
    }

    void drawPlane() {
        Color.blue.bind();
        glBegin(GL_POLYGON);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 6f, 0f);
        glVertex3f(6f, 6f, 0f);
        glVertex3f(6f, 0f, 0f);
        glEnd();
        TextureImpl.unbind();

    }

    void drawCube() {
        glBegin(GL_QUADS); // FIXME: Replace with GL_TRIANGLES
        glColor3f(1f, 0f, 0f);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0.5f, 0f);
        glVertex3f(0.5f, 0.5f, 0f);
        glVertex3f(0.5f, 0f, 0f);

        glColor3f(0f, 1f, 0f);
        glVertex3f(0f, 0f, 0.5f);
        glVertex3f(0f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0f, 0.5f);

        glColor3f(0f, 0f, 1f);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0f, 0.5f);
        glVertex3f(0.5f, 0f, 0.5f);
        glVertex3f(0.5f, 0f, 0f);

        glColor3f(0f, 1f, 1f);
        glVertex3f(0f, 0.5f, 0f);
        glVertex3f(0f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0f);

        glColor3f(1f, 0f, 1f);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0f, 0.5f);
        glVertex3f(0f, 0.5f, 0.5f);
        glVertex3f(0f, 0.5f, 0f);

        glColor3f(0f, 1f, 1f);
        glVertex3f(0.5f, 0f, 0f);
        glVertex3f(0.5f, 0f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0f);
        glEnd();
    }

    void poll() {
        int eventCtr = 0;
        while (Mouse.next()) {
            int event = Mouse.getEventButton();
            switch(event) {
                case (-1): {
                    if (Mouse.isButtonDown(0)) {
                        glRotatef_angle = (glRotatef_angle > 360) ? 0f : glRotatef_angle+Mouse.getEventDX();
                        glRotatef_angley = (glRotatef_angley > 360) ? 0f : glRotatef_angley+Mouse.getEventDY();
                    } else if (Mouse.isButtonDown(1)) {
                        gluLookAt_eyex -= Mouse.getEventDY() / 3f;
                        gluLookAt_centerx -= Mouse.getEventDY() / 3f;
                        gluLookAt_eyez -= Mouse.getEventDX() / 3f;
                        gluLookAt_centerz -= Mouse.getEventDX() / 3f;
                        debug3("eyex=%f, centerx=%f, eyez=%f, centerz=%f",
                                gluLookAt_eyex, gluLookAt_centerx, gluLookAt_eyez, gluLookAt_centerz);
                    }
                    break;
                }
                default: {
                    /* Click */
                    debug2("clicked mbutton=%d", event);
                    break;
                }
            }
            eventCtr++;
        }

        while (Keyboard.next()) {
            int event = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                switch (event) {
                    case (Keyboard.KEY_ESCAPE): {
                        exit();
                        break;
                    }
                }
            }
            eventCtr++;
        }
        debug("poll() eventCtr=%d", eventCtr);
    }
}
