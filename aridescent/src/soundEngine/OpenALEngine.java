package soundEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import org.lwjgl.util.WaveData;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.lwjgl.openal.AL10.*;

public final class OpenALEngine {

	private static int source, buffer; 

	public static void init(){
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			AL.destroy();
			System.exit(1);
		}
	}

	public static void play(String file_name) throws FileNotFoundException {
		WaveData data = WaveData.create(new BufferedInputStream(new FileInputStream("res/" + file_name)));
		buffer = alGenBuffers();
		alBufferData(buffer, data.format, data.data, data.samplerate);
		data.dispose();

		source = alGenSources();
		alSourcei(source, AL_BUFFER, buffer);
		alSourcePlay(source); //play sound

		alDeleteBuffers(buffer);
	}

	/** 0 is minimum (muted) & 1 is max
	 * @param volume */
	public static void setVolume(float volume){ alSourcef(source, AL_GAIN, volume); }

	/** 0 is minimum, 1 is normal pitch and goes on to infinity and beyond from here on!
	 * (or up to max float value...)
	 * @param pitch */
	public static void setPitch(float pitch){ alSourcef(source, AL_PITCH, pitch); }
	
	public static void close(){
		alDeleteSources(source);
		AL.destroy();
	}
}