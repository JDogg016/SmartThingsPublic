 /**
 *  Kill The Laundry Room
 *
 *  Author: JDogg016
 */
  


// Automatically generated. Make future change here.
definition(
    name: "Kill the Laundry Room",
    namespace: "laundry",
    author: "JDogg016",
    description: "Turn off the Laundry Room Light When Another Light is Turned On.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_motion-outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_motion-outlet@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_motion-outlet@2x.png")

preferences {
	section("When I turn on any of these switches...") {
		input "motion1", "capability.switch", title: "Which ones?", multiple: true
	}
	section("Turn off the Laundry Room Light") {
		input "switch1", "capability.switch"
	}
    section("embedded") {
        href(name: "hrefWithImage", title: "This element has an image and a long title.",
             description: "tap to view SmartThings website inside SmartThings app",
             required: false,
             image: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
             url: "http://smartthings.com/")
	}
   }

def installed()
{
	subscribe(motion1, "switch", motionInactiveHandler)
}

def updated()
{
	unsubscribe()
	subscribe(motion1, "switch", motionInactiveHandler)
}

def motionInactiveHandler(evt) {
	switch1.off()
}
