/**
 *  Switch Activates Hello, Home Phrase
 *
 *  Copyright 2015 Michael Struck
 *  Version 1.0.1 3/8/15
 *  Version 1.0.2 4/24/15 added the ability to rename the app and limit it to certain modes
 *  Version 1.0.3 5/31/15 Added an About Screen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Ties a Hello, Home phrase to a switch's (virtual or real) on/off state. Perfect for use with IFTTT.
 *  Simple define a switch to be used, then tie the on/off state of the switch to a specific Hello, Home phrases.
 *  Connect the switch to an IFTTT action, and the Hello, Home phrase will fire with the switch state change.
 *
 *
 */
definition(
    name: "Switch Activates Air Conditioner",
    namespace: "Home",
    author: "Justin Bennett",
    description: "Turns on the A/C (sets to Home) with a push of a button or IFTTT",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/IFTTT-SmartApps/App1@2x.png")


preferences {
	
		input "controlSwitch", "capability.switch", title: "Switch", multiple: false, required: true
    }
   
    section("Select Thermostat you want to use") {
        input "thermostat", "capability.thermostat", title: "Thermostat", required: true, multiple: true
        
        }
        

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribe(controlSwitch, "switch", "switchHandler")
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe(controlSwitch, "switch", "switchHandler")
}

def switchHandler(evt) {
	if (evt.value == "on") {

log.debug "Switch Has Been Pressed"
            
    	   	thermostat.present()
        	

        	log.debug "Putting thermostat in HOME you pressed the button."
            } else {
            thermostat.away()
            log.debug "Putting Thermostate in AWAY mode because you pressed the button"

}
}