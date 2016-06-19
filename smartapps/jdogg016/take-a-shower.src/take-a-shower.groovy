/**
* Lights Off, When Closed
*
* Author: SmartThings
*/
definition(
name: "Take A Shower",
namespace: "JDogg016",
author: "Justin Bennett",
description: "When you walk into the bathroom the bathroom light will come on. When you close the shower door, the bathroom light will go off after 5 seconds and the shower light will come on and the motion detection will be disabled for 20 minutes (during the shower). When you open the door, motion detection will re-enable and the light will come on.",
category: "Convenience",
iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet.png",
iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet@2x.png"
)

preferences {
section ("When the door closes...") {
input "contact1", "capability.contactSensor", title: "Where?"
}
section ("Virtual Switch goes here...") {
input "switch3", "capability.switch", title: "Virtual Switch"

}
section ("Turn on the shower light...") {
input "switch1", "capability.colorControl", title: "Shower Light"
}
section ("And turn off the bathroom light..."){
input "switch2", "capability.switch", title: "Bathroom Dimmer"
}
section ("Disable the bathroom motion sensor..."){
input "motion1", "capability.motionSensor", title: "Which Motion Sensor"

}
} 
def initialize() {
subscribe(switch1, "switch", showerSwitch)
subscribe(switch1, "setColor",colorHandler)
subscribe(switch2, "switch", bathroomSwitch)
subscribe(contact1, "contact.open", showerOpen)
subscribe(contact1, "contact.closed", showerClosed)
subscribe(motion1, "motion", motionHandler)
subscribe(switch3, "switch", virtualSwitch)
state.enabled = true



}
def installed()
{
log.debug "Intalled with settings ${settings}"
initialize ()

}
def updated(){
log.debug "Updated with settings ${settings}"
unsubscribe()
initialize()

state.enabled = true

}
def enable() {
log.trace "Motion enabled"
state.enabled = true

}
def disable() {
log.trace "Motion disabled"
state.enabled = false

}
def motionHandler(evt) {
	log.debug "Motion Handler - Evt value: ${evt.value}"
    if (evt.value == "active") {
    	if (state.enabled) {
        	log.debug("Turning on lights")
            switch2.on()
            switch3.on()
            //switch2.off([delay: 600 * 1000])
        } else {
        	log.debug("Motion is disabled - not turning on lights")
        }
    }

}
def showerSwitch(evt) {
def bathroomState = switch2.currentValue("switch")
def showerState = switch1.currentValue("switch")
	if (evt.value == "on") {
        log.debug "The Shower Light is ${showerState} and the bathroom light is ${bathroomState}"
	} else {
		log.debug "The Shower Light is ${showerState} and the bathroom light is ${bathroomState}"
	}
}
def bathroomSwitch(evt) {
def bathroomState = switch2.currentValue("switch")
def showerState = switch1.currentValue("switch")
	if (evt.value == "on"  && evt.isPhysical()){
    	log.debug "The Bathroom Light is ${bathroomState} and the Shower Light is ${showerState}"
  	} else {
    	log.debug "Switch Handler - Evt value: ${evt.value}, isPhysical: ${evt.isPhysical()}"
    if (evt.value == "off" && evt.isPhysical()) {
    	log.debug "Motion disabled"
    	state.enabled = true  
        switch1.off()
        switch3.on()
        log.debug "The Bathroom Light is ${bathroomState} and the Shower Light is ${showerState} and the Virtual Switch is now back on"  
	}
    }
}

def showerOpen(evt){
def bathroomState = switch2.currentValue("switch")
def showerState = switch1.currentValue("switch")
def vsOn = switch3.currentValue("switch")
def twentyMinutes = 12000
if (evt.value == "open" && bathroomState == "on" && showerState == "off"  && vsOn == "on") {
log.debug "The Shower Door is open and the bathroom light is on and the shower light is off so I am not really doing anything"
    log.debug "switch 3 is ${vsOn}"
    } else {
		if (evt.value == "open" && bathroomState == "off" && showerState == "on"  && vsOn == "off") {
        log.debug "The Shower Door is open and the bathroom light is off and the shower light is on so I am turning motion ${state.enable} because you are getting out of the shower"
         log.debug "switch 3 is ${vsOn}"
        switch1.off([delay:35])
        switch3.on()
        switch2.on([delay:1])
        enable()
	}
}
}

def showerClosed(evt){
def vsOn = switch3.currentValue("switch")
def bathroomState = switch2.currentValue("switch")
def showerState = switch1.currentValue("switch")
def motionState = motion1.currentState("motion")
def colorMap = [hue:new Random().nextInt(99) + 1,saturation:new Random().nextInt(99) + 1]
//def newestColor = colorMap + Math.abs(new Random().nextInt() % 500)
if (evt.value == "closed" && bathroomState == "on"  && showerState == "off"  && vsOn == "on"){
def showerOn = switch1.setLevel(99)
def twoSeconds = ([delay:200])
//showerOn(twoSeconds)
//showerOn
//switch1.setSaturation(ranSat)
//switch1.setHue(ranHue)
switch1.on([delay:2])
switch1.setColor(colorMap)
//switch1.setColor(colorMap,[delay: 4 * 100])
switch2.off ()
switch3.off()

// runIn(1,colorMap)
 
 disable()
    
	//switch1.setColor(colorMap)

  
 log.debug "switch 3 is ${vsOn}"
log.debug "Door is Closed because you ARE taking a shower"


	} else {
if (evt.value == "closed" && showerState == "off"  && vsOn == "off"){
	enable()
    switch2.on()
    switch3.on()
    switch1.off()
 log.debug "switch 3 is ${vsOn}"
	 
   
   }
}
}


