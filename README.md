# Idea:
For our project, we would like to build an Android web application using the NeuroSky
headset. The function of our application would be to play music at specific tempos to help the
user achieve desired brain states. The average values of the raw data over the given time
period would then be output to a visualization. The user will be able to view these recordings
and possibly adjust the tempo associated with the desired state accordingly. To achieve this, we
can use the Spotify music API to get songs based on tempo. We will also use another Android
based library for the visualization (most likely a radar chart). The following scenario describes
the typical use case we would like to achieve: An individual needs to study and wants to
increase their alpha wave activity. They choose the corresponding mode on the application and
music is played within a specific beats per minute (BPM) interval. Afterwards, they refer back to
the radar chart to see what their average brain state was. Ideally, we would like to add a
functionality that also allows the user to increase or decrease the tempo based on the results.
However, this will depend on time constraints.


# Inspiration:
During our group discussion, one of our team members brought up the idea that during
school, many students are having difficulty focusing on their studies because they are either in a
state of drowsiness or hyperness. We also know the capabilities of NeuroSky’s MindWave EEG
headset which can provide us with information about users’ brain activity state. We then came
up with the idea of a music app where user will be able to select the state they want to be in for
the next hour or so. The app will constantly monitor user’s brain activity and play corresponding
music so that user stays in their desired brain state.


# Hypothesis:
Using NeuroSky’s MindWave EEG headset, brain state information can be tracked
through NeuroSky’s Brainwave Visualizer app and sent to the application as input. We know
that brain oscillations are categorized (delta, theta, alpha, beta, and gamma) according to
frequencies (0-4, 4-7, 8-12, 12-30, and 30+ Hz, respectively). We also know that these
categories are representative of different levels of wakefulness. With the understanding that the
MindWave headset can track these oscillations, we decided to use these oscillations as input
and send them through the bluetooth to our application integrated with NeuroSky’s Android
development kit. From there, we can transfer the data at a certain rate, process and translate
the it, then send an API request to Spotify to get songs that will help user to achieve the
oscillations of their desire.


# Video Walkthrough:
[Link to Youtube Video](https://youtu.be/V0hr8A_bs40)
