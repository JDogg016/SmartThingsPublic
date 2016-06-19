/**
 *  Scheduled Mode Change - Presence Optional
 *
 *  Author: SmartThings

 *
 */

definition(
    name: "Shower Time",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Changes mode when you take a shower to alter the state of the bathroom.",
    category: "Mode Magic",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld@2x.png"
)

preferences {
	section("When Humidity Rises Above") {
		input "humiditysensor1", "capability.relativeHumidityMeasurement"
	}
    section ("When Humidity Rises By"){
    	input "humidity1", "number", title: "Percentage?"
    
    }
    section("Change to this mode") {
		input "newMode", "mode", title: "Mode?"
	
//put Pushbullet Notification HERE!
    }
	section( "Notifications" ) {
        input("recipients", "contact", title: "Send notifications to") {
            input "sendPushMessage", "enum", title: "Send a push notification?", options: ["Yes", "No"], required: false
            input "phoneNumber", "phone", title: "Send a text message?", required: false
        }
	}
}

def installed() {
	subscribe(humiditySensor1, "humidity", humidityHandler)
    initialize()
}

def updated() {
	unschedule()
    subscribe(humiditySensor1, "humidity", humidityHandler)
	initialize()
}

def initialize() {
	schedule(time, changeMode)
}
def humidityHandler(evt) {
	log.trace "humidity: ${evt.value}"
    log.trace "set point: ${humidity1}"

	def currentHumidity = Double.parseDouble(evt.value.replace("%", ""))
	def tooHumid = humidity1 
    def changemode = (location.mode !=newMode)
	def mySwitch = settings.switch1
 def timeAgo = new Date(now() - (1000 * 60 * deltaMinutes).toLong())
	def recentEvents = humiditySensor1.eventsSince(timeAgo)
	log.trace "Found ${recentEvents?.size() ?: 0} events in the last ${deltaMinutes} minutes"
	def alreadySentSms = recentEvents.count { Double.parseDouble(it.value.replace("%", "")) >= tooHumid } > 1 || recentEvents.count { Double.parseDouble(it.value.replace("%", "")) <= notHumidEnough } > 1
    
	if (currentHumidity >= tooHumid) {
		log.debug "Checking how long the humidity sensor has been reporting >= ${tooHumid}"

		// Don't send a continuous stream of text messages
		


		if (alreadySentSms) {
			log.debug "Notification already sent within the last ${deltaMinutes} minutes"
			
		} else {
			log.debug "Humidity Rose Above ${tooHumid}:  sending SMS to $phone1 and activating ${mySwitch}"
			send("${humiditySensor1.label} sensed humidity rose above ${evt.value}")
			newMode()
		}
	}
}
def changeMode() {
	log.debug "changeMode, location.mode = $location.mode, newMode = $newMode, location.modes = $location.modes"
	if (location.mode != newMode) {
		if (location.modes?.find{it.name == newMode}) {
			setLocationMode(newMode)
			send "${label} has changed the mode to '${newMode}'"
		}
		else {
			send "${label} tried to change to undefined mode '${newMode}'"
		}
	}
}

private send(msg) {

    if (location.contactBookEnabled) {
        log.debug("sending notifications to: ${recipients?.size()}")
        sendNotificationToContacts(msg, recipients)
    }
    else {
        if (sendPushMessage == "Yes") {
            log.debug("sending push message")
            sendPush(msg)
        }

        if (phoneNumber) {
            log.debug("sending text message")
            sendSms(phoneNumber, msg)
        }
    }

	log.debug msg
}

private getLabel() {
	app.label ?: "SmartThings"
}
