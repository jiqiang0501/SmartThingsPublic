/**
 *  Htmlmodules
 *
 *  Copyright 2016 jiqiang
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
 */
definition(
    name: "Htmlmodules",
    namespace: "Test",
    author: "jiqiang",
    description: "This is a program for test",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "mainPage", title: "Test Setup", install: true, uninstall: true) {
		section {
			app "buttons", "smartthings/ButtonContainer", "Big Button",
			title: "Button configuration", page: "mainPage", multiple: true,
		install: true
		}
	}
}

cards {
	card(name: "home", type: "html", action: "home", whitelist:whitelist()) {}
	if (app.id) {
		childApps.each {
        	childApp -> 
			card(name: childApp.label, type: "html", action: "home",
			whitelist:whitelist(), installedSmartAppId: childApp.id) {}
		}
	}
	card(name: "about", type: "html", action: "about", whitelist:whitelist()) {}
}

mappings {
	path("/home") {
		action: [
			GET: "home"
		]
	}
	path("/about") {
		action: [
			GET: "about"
		]
	}
}

def home() {
	renderHTML("test") {
		head {
			"""
			"""
		}
		body {
			"""
			this is the parent app home page.
			"""
		}
	}
}

def about() {
	renderHTML("test") {
		head {
			"""
			"""
		}
		body {
			"""
			this is the parent app about page.
			"""
		}
	}
}

def whitelist() {
	return [
		"ajax.googleapis.com",
		"d102a5bcjkdlos.cloudfront.net"
	]
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	createAccessToken()
	initialize()
}

def uninstalled() {
	revokeAccessToken()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    updateSolutionSummary()
}

// TODO: implement event handlers
def updateSolutionSummary() {
	def summaryData = []
	summaryData << ["icon":"indicator-dot-green","iconColor":"#49a201","default":"true"
		,"value":"Button is working"]
	sendEvent(linkText:app.label, descriptionText:app.label +
		" updating summary", eventType:"SOLUTION_SUMMARY",
		name: "summary", value: summaryData*.value?.join(", "),
		data:summaryData, isStateChange: true, displayed: false)
}