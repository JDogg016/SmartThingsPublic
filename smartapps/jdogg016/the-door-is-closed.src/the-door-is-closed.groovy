/**
 *  Thermostat Auto Home
 *
 *  Author: sidjohn1@gmail.com
 *  Date: 2013-07-23
 */

// Automatically generated. Make future change here.
definition(
    name: "The Door is Closed",
    namespace: "JDogg016",
    author: "justin.bennett@nyu.edu",
    description: "Turns the Thermostat to Home when in Home Mode and that door is finally shut",
    category: "Green Living",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    oauth: false
)

preferences {

	section("When the door closes") {
		input "contact1", "capability.contactSensor", title: "Where?"
	}
	section("Set this thermostat staus to home") {
		input "thermostat1", "capability.thermostat", title: "Which?", multiple: true, required: true
    }
}

def installed()
{
	subscribe(contact1, "contact", contactHandler)
}

def updated()
{
	unsubscribe()
	subscribe(contact1, "contact", contactHandler)
}

def contactHandler(evt)
{
	log.debug "contactHandler $evt.name: $evt.value"
	for (t in settings.thermostat1) {
	if (evt.value == "closed"){
    	setHome()
		
      } else
       log.debug "I am doing nothing because the door is open"
	}
    }

def setHome() {
	for (t in settings.thermostat1) {
	if (thermostat1[0].lastestValue("presence") == "away") {
	log.debug "SmartThings changed your thermostat to home because the door is shut"
	t.present()
    }
    }
 }   
    

