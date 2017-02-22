package me.mfroehlich.frc.abstractions;

import me.mfroehlich.frc.abstractions.live.LiveEnvironment;

class EnvironmentManager {
	public static Environment current = new LiveEnvironment();
}
