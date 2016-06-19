/**
* Lights Off, When Closed
*
* Author: SmartThings
*/
definition(
name: "Matthew's NightLight",
namespace: "JDogg016",
author: "Justin Bennett",
description: "When you hit the virtual switch, Matthew's Night Light Comes on and his desk lamp goes to a soft blue.",
category: "Convenience",
iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet.png",
iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet@2x.png"
)

preferences {

section ("Virtual Switch goes here...") {
input "switch3", "capability.switch", title: "Virtual Switch"

}
section ("Turn on the desk light...") {
input "switch1", "capability.colorControl", title: "Desk Light"
}
section ("And turn on the night light..."){
input "switch2", "capability.switch", title: "Night Light"


}
} 
def initialize() {
subscribe(switch1, "switch", deskSwitch)
subscribe(switch1, "setColor",colorHandler)
subscribe(switch2, "switch", nightSwitch)
subscribe(switch3, "switch", virtualSwitch)

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
def virtualSwitch(evt) {
def colorMap = [red:0, level:null, hex:0028F, blue:255, saturation:100, hue:69.99346405228758, green:40, alpha:1]
	if (evt.value == "on") {
        log.debug "The Virtual Switch is ${showerState} and the desk light is ${bathroomState}"
        switch1.setLevel(20)
        switch1.setColor(colorMap)
        switch2.on()
	} else {
		log.debug "The Virtual Switch is ${showerState} and the desk light is ${bathroomState}"
        switch1.off()
        switch2.off()
	}


   
   }
