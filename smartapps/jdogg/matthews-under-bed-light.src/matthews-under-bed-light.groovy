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
 *  Turn It On When It Opens
 *
 *  Author: SmartThings
 */
definition(
    name: "Matthew's Under Bed Light",
    namespace: "JDogg",
    author: "JDogg",
    description: "Turn something on when an open/close sensor opens.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet@2x.png"
)

preferences {
	section("When the door opens..."){
		input "contact1", "capability.contactSensor", title: "Where?"
	}
	section("Turn on a light..."){
		input "switches", "capability.colorControl", multiple: false
	}
}


def init()
{
	subscribe(contact1, "contact.open", contactOpenHandler)
    subscribe(contact1, "contact.closed", contactClosedHandler)
    subscribe(switches, "setColor",colorHandler)
    subscribe(switches, "setLevel",colorHandler)
 
}

def updated()
{
	unsubscribe()
	subscribe(contact1, "contact.open", contactOpenHandler)
    subscribe(contact1, "contact.closed", contactClosedHandler)
}

def contactOpenHandler(evt) {
	log.debug "Event Value: $evt, $settings"
	log.trace "Turning on switches: $switches"
	switches.on()
  
    
   def colorMap = [hue:new Random().nextInt(99) + 1,saturation:new Random().nextInt(99) + 1]
   def colorMaps = [hue:new Random().nextInt(99) + 1,saturation:new Random().nextInt(99) + 1]
   def secondColor = switches.setColor(colorMaps)
   def oneMinuteDelay = 60
   switches.setColor(colorMap)
   //runIn(3,secondColor)
    switches.setColor(colorMaps)
   
   //switches.setColor(colorMaps,[delay: 3])
   switches.setColor(newColor)
   
    
  
    
    
	   
    log.debug "Light is on and $colorMap"
  //runIn(1,changeColor)
}

def contactClosedHandler(evt) {
	log.debug "Event Value: $evt, $settings"
    log.debug "The lights should turn off in 2 minutes -- God willing."
    switches.setColor(colorMaps)
    def twoMinuteDelay = 20 //this controls that the switch will turn off in 20 sec
	def secondOff = 30
    runIn(twoMinuteDelay, turnOffSwitch)
    
}

def turnOffSwitch() {
	//switches.setLevel(0)
    switches.off()
   
    
    
    }
    
  