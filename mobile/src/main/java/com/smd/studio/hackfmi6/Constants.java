package com.smd.studio.hackfmi6;

/**
 * Created by Doychev on 15.5.2015.
 */
public interface Constants {
    String USERNAME = "pi";
    String PASSWORD = "raspberry";
    String WEBCAM_PORT_AND_ACTION = ":8080/?action=stream";
    String FORWARD = "sudo python /home/pi/Projects/HackFMI6.py w";
    String FORWARD_LEFT = "sudo python /home/pi/Projects/HackFMI6.py a";
    String FORWARD_RIGHT = "sudo python /home/pi/Projects/HackFMI6.py d";
    String BACKWARD = "sudo python /home/pi/Projects/HackFMI6.py s";
    String BACKWARD_LEFT = "sudo python /home/pi/Projects/HackFMI6.py c";
    String BACKWARD_RIGHT = "sudo python /home/pi/Projects/HackFMI6.py z";
    String STOP = "sudo python /home/pi/Projects/HackFMI6.py x";
    String INCREASE_SPEED = "sudo python /home/pi/Projects/HackFMI6.py n";
    String DECREASE_SPEED = "sudo python /home/pi/Projects/HackFMI6.py m";
    String AUTONOMOUS_DRIVING_ON = "sudo python /home/pi/Projects/HackFMI6.py o";
    String AUTONOMOUS_DRIVING_OFF = "sudo python /home/pi/Projects/HackFMI6.py p";
    String TURN_ON_CAMERA = "/home/pi/k/mjpg-streamer-code-182/mjpg-streamer/mjpg_streamer -i \"./input_uvc.so -f 15 -r 320x240\" -o \"./output_http.so -w ./www";
}