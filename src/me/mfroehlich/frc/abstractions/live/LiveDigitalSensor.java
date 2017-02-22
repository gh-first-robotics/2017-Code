package me.mfroehlich.frc.abstractions.live;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.Timer;
import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.eventloop.events.Event;

class LiveDigitalSensor implements DigitalSensor {
	private DigitalInput digital;
	
	private boolean lastValue;
	private Event event = new Event();
	
	/**
	 * Creates a new digital sensor to wrap a break beam or limit switch
	 * @param id the io port for the wrapped sensor
	 */
	public LiveDigitalSensor(int id) {
		digital = new DigitalInput(id);
		lastValue = !digital.get();
		
		digital.requestInterrupts(new InterruptHandlerFunction<Object>() {
			public void interruptFired(int interruptAssertedMask, Object param) {
				boolean val = !digital.get();
				if (val == lastValue) {
					return;
				}
				
				lastValue = val;
				event.emit();
			}
		});
		
		digital.setUpSourceEdge(true, true);
		digital.enableInterrupts();
	}
	
	@Override
	public Observer observe(boolean target) {
		return new Observer(target);
	}
	
	/**
	 * Gets the state of the break beam
	 * @return returns true if the beam is broken, false if not
	 */
	@Override
	public boolean value() {
		return lastValue;
	}

	/**
	 * Prints the value of the sensor every second for debugging purposes
	 */
	@Override
	public void debug() {
		new Thread(() -> {
			while (true) {
				System.out.println(digital.getChannel() + ": " + digital.get());
				
				Timer.delay(1);
			}
		}).start();
	}
	
	private class Observer implements DigitalSensor.Observer {
		private boolean target;
		private boolean value;
		
		public Observer(boolean target) {
			this.target = target;
			this.reset();
			
			event.listen(this::update);
		}
		
		private void update() {
			if (LiveDigitalSensor.this.value() == target) {
				value = target;
			}
		}
		
		@Override
		public boolean value() {
			return value;
		}

		@Override
		public void reset() {
			value = !target;
			this.update();
		}
	}
}
