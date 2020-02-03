from evdev import InputDevice, categorize, ecodes, KeyEvent
gamepad = InputDevice('/dev/input/event3')
for event in gamepad.read_loop():
	event_r = str(event)
	if(not("code 00" in event_r) and not("code 04" in event_r)):
		print(event_r)
