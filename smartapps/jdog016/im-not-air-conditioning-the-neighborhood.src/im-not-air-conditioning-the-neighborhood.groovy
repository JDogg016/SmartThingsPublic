/**
 *  Copyright 2015 SmartThings
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
 *  Left It Open
 *
 *  Author: SmartThings
 *  Date: 2013-05-09
 */
definition(
    name: "I'm NOT Air Conditioning the Neighborhood",
    namespace: "JDog016",
    author: "JDogg016",
    description: "When you leave a door open too long, the thermostat is set to away.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage%402x.png"
)

preferences {
	section("Select Motion Sensor(s) you want to use") {
        input "motions", "capability.motionSensor", title: "Motion Detector", required: false, multiple: true
	}
    section("Select Contact Sensor(s) you want to use") {
        input "contacts", "capability.contactSensor", title: "Contact Sensor", required: false, multiple: true
	}
    section("Select Thermostat you want to use") {
        input "thermostat", "capability.switch", title: "Select Thermostat Switch", required: true, multiple: false
	}
    
        input "DelayMinStr", "number", title: "Turn on AWAY mode after how many minutes?", required: true, 
        	defaultValue: "120"
        	
}
def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	log.debug "Initialize..."
   
    state.DelayMin = DelayMinStr
    state.HomeMotion=true
    state.HomeContactOpen=false
    

    subscribe(motions, "motion.active", handleMotionEvent)
    subscribe(motions, "motion.inactive", handleEndMotionEvent)
	subscribe(contacts, "contact.open", ContactOpen)
    subscribe(contacts, "contact.closed", ContactClosed)

// contact closed - start
}


def ContactClosed(evt) {
	log.debug "Contact is closed."
            
    	   	thermostat.on()
        	state.HomeContactOpen=false

        	log.debug "Putting thermostat in HOME mode because of Contact."
        
       
	}


// contact closed - end

// contact open - start
def ContactOpen(evt) {   
	
    log.trace "contactOpen($evt.name: $evt.value)"
	def t0 = now()
	def delay = (DelayMinStr != null && DelayMinStr != "") ? DelayMinStr * 60 : 600
	runIn(delay, putAwayMode)
	log.debug "scheduled putAwayMode in ${now() - t0} msec"
    state.HomeContactOpen=true
    
   // log.debug "Door or Window is Open."
	// log.debug "Putting thermostat in AWAY mode because of Contact in 1 minute." 
    // runIn((state.DelayMin*60), putAwayMode)
    //thermostat?.away()
    //state.HomeContactOpen=true

}

// contact open - end


def handleMotionEvent(evt) {
	log.debug "Motion detected."

    unschedule(putAwayMode)
    
    
    if (state.HomeContactOpen) {
        log.debug "Thermostat already in AWAY mode due to CONTACT (do nothing)."
        }
    else if (state.HomeMotion) {
        log.debug "Thermostat already in HOME mode due to MOTION (do nothing)."
        }
         else {
    	   	thermostat?.present()
        	state.HomeMotion=true
        	log.debug "Putting thermostat in HOME mode because of Motion."
        }
	}

def handleEndMotionEvent(evt) {
	log.debug "Motion stopped."
    log.debug "Start countdown timer to AWAY mode."

    if (state.HomeMotion) {
    	runIn((state.DelayMin*60), putAwayMode)
       }

}

def putAwayMode() {   
if (state.HomeContactOpen){
	log.debug "Putting thermostat in AWAY mode because of Motion delay." 
    thermostat.off()
    state.HomeMotion=false
    
    }
    else {
    thermostat?.present()
    state.HomeContactOpen=false
    }
    }
 
