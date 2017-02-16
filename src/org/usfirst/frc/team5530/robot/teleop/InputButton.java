package org.usfirst.frc.team5530.robot.teleop;

public enum InputButton {
    Raise_Arm(0, 1), //temporarily disabling arms to prevent them from messing up the camera
    Lower_Arm(0, 2),
    Reverse_Steering(0, 7),
    Drive_Towards_Target(0,3),
    //	Record(0, 12),
    
    Enable_Gear_Movement(1,1),
    Intake_Gear(1, 3),
    Place_Gear(1, 4),
    Zero_Position(1, 5),
    Turn_Off(1,6),
    Toggle_Scaler(1, 7),
    Lock_Scaler(1, 9),
    Unlock_Scaler(1, 10);
    
    public final int stick;
    public final int button;
    
    InputButton(int stick, int button) {
        this.stick = stick;
        this.button = button;
    }
    
    public static InputButton get(int stick, int button) {
        for (InputButton butt : values())
            if (butt.stick == stick && butt.button == button)
                return butt;
        throw new IllegalArgumentException("Stick " + stick + " button " + button + " not found");
    }
}
