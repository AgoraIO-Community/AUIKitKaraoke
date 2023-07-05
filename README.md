# AUIKitKaraoke

*English | [中文](README.zh.md)*

## Overview

**AUIKit Component Solution** is a low-code solution launched by Agora for Karaoke, VoiceRoom and other scenarios. By utilizing Agora's RTC && RTM technology capabilities, it helps developers quickly implement related business requirements and enhance the core business.

## Scenario Description

AUIKitKaraoke is a functional component solution that integrates Agora's RTC, RTM, copyright songs and other products, helping you to quickly develop online Karaoke rooms. In this solution, the anchor can select a song to become the lead singer and sing with the accompaniment to the audience in the room. The room includes components such as lyrics display, rating, song selection, player, and microphone seats. Agora's copyright songs provides a library of over 200,000 songs, and the connected anchor can search for, select, and view the playlist. Audience members can queue up to sing, interact with the host and other connected anchors in real-time audio.


| Role     | Description                                           |
| -------- | ---------------------------------------------- |
| Host     | Creator of the room                                     |
| CoSinger | A connected anchor who joins and sings along with the lead singer            |
| LeadSinger     | A connected anchor who selects a song, joins the queue to sing, and is currently performing |
| Audience     | A listener who enters the room, either as a listener or as a connected anchor                               |

AUIKitKaraoke provides the following core features:
- **Room management**：Create, destroy, and list rooms
- **Microphone seat management:**：Manage anchors' microphone access, including granting access, revoking access, kicking off, banning/locking microphones, etc
- **Music player**：Controls music playback, including play, pause, skip to next song
- **Song management**： Search for songs, select, add to playlist, skip, and reorder songs
- **Chorus**：Join/leave chorus, synchronize lyrics display



## Demo
![](https://download.agora.io/null/uikit_karaoke_demo_pic1.jpg)|![](https://download.agora.io/null/uikit_karaoke_demo_pic3.jpg)|
---|---

| iOS                                                          | Android                                                      |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://download.agora.io/demo/release/iOS_uikit_karaoke_0.1.0.png) | ![](https://download.agora.io/demo/release/android_uikit_karaoke_demo_1.0.0.png) |

## Quick Start

| iOS | Android | Backend |
| --- | --- | --- |
| [Karaoke（iOS）](https://github.com/AgoraIO-Community/AUIKitKaraoke/tree/main/iOS/Example) | [Karaoke（Android）](https://github.com/AgoraIO-Community/AUIKitKaraoke/tree/main/Android)  | [Karaoke（Backend）](https://github.com/AgoraIO-Community/AUIKitKaraoke/tree/main/backend) |


## Communication&Feedback

Welcome to join our WeChat communication group

![image-20210622142449407](https://download.agora.io/null/karaoke-uikit-wechat-pic.jpg)



---

## FAQ

### How to Get Agora APPID

> Apply for Agora AppID：[https://www.agora.io/cn/](https://www.agora.io/cn/)


### How to Contact Agora for Support

> Solution 1: Join our WeChat communication group
>
> Solution 2: Send an email to support@agora.io for consultation when you encounter integration difficulties.

---