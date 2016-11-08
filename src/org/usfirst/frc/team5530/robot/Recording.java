package org.usfirst.frc.team5530.robot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.usfirst.frc.team5530.robot.input.ControlState;
import org.usfirst.frc.team5530.robot.input.InputButton;
import org.usfirst.frc.team5530.robot.input.Vector2;

import edu.wpi.first.wpilibj.Timer;

public class Recording {
	private List<Record> records = new ArrayList<>();
	private boolean isRecording;

	private long endRecordingTime;

	private int playbackIndex;

	public void startRecording() {
		isRecording = true;
		endRecordingTime = System.currentTimeMillis() + 15000;
	}

	public void record(ControlState state) {
		if (!isRecording())
			return;
		long diff = System.currentTimeMillis() - records.get(records.size() - 1).delay;
		records.add(new Record(diff, state));
	}

	public boolean isRecording() {
		long diff = endRecordingTime - System.currentTimeMillis();
		if (isRecording && diff <= 0) {
			isRecording = false;
			System.out.println("Recording finished");
		}
		return isRecording;
	}

	public boolean isDoneRecording() {
		return !isRecording() && size() > 0;
	}

	public int size() {
		return records.size();
	}

	public void startPlayback() {
		playbackIndex = 0;
	}

	public ControlState play() {
		Record record = records.get(playbackIndex);
		playbackIndex++;
		Timer.delay(record.delay / 1000.0);
		return record.state;
	}

	public byte[] serialize() {
		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(buff);
		try {
			data.writeInt(size());
			for (Record record : records) {
				record.serialize(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toByteArray();
	}

	public static Recording deserialize(byte[] raw) {
		ByteArrayInputStream buff = new ByteArrayInputStream(raw);
		DataInputStream data = new DataInputStream(buff);
		try {
			Recording recording = new Recording();
			int size = data.readInt();
			for (int i = 0; i < size; i++) {
				Record rec = Record.deserialize(data);
				recording.records.add(rec);
			}
			return recording;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class Record {
		private long delay;
		private ControlState state;

		private Record(long time, ControlState state) {
			this.delay = time;
			this.state = state;
		}

		public void serialize(DataOutputStream data) throws IOException {
			data.writeLong(delay);

			int sticks = state.getStickCount();
			data.writeInt(state.getStickCount());
			for (int i = 0; i < sticks; i++) {
				Vector2 stick = state.getStick(i);
				data.writeDouble(stick.x);
				data.writeDouble(stick.y);
			}
			Collection<InputButton> buttons = state.getButtons();
			data.writeInt(buttons.size());
			for (InputButton button : buttons) {
				data.writeInt(button.stick);
				data.writeInt(button.button);
				data.writeBoolean(state.isPressed(button));
			}
		}

		public static Record deserialize(DataInputStream data) throws IOException {
			long delay = data.readLong();
			int count = data.readInt();
			Vector2[] sticks = new Vector2[count];
			for (int j = 0; j < count; j++) {
				double x = data.readDouble();
				double y = data.readDouble();
				sticks[j] = new Vector2(x, y);
			}
			count = data.readInt();
			HashMap<InputButton, Boolean> map = new HashMap<>();
			for (int j = 0; j < count; j++) {
				int stick = data.readInt();
				int index = data.readInt();
				InputButton button = InputButton.get(stick, index);
				map.put(button, data.readBoolean());
			}
			return new Record(delay, new ControlState(sticks, map));
		}
	}
}
