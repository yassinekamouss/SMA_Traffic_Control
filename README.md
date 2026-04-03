# 🚦 Intelligent Traffic Control System (JADE MAS)

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.11-blue)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-green)

<details>
<summary>Table of Contents</summary>

1. [📌 Short Description](#-short-description)
2. [✨ Key Features](#-key-features)
3. [🏗️ Project Architecture](#-project-architecture)
4. [⚙️ Prerequisites](#-prerequisites)
5. [🚀 Installation & Configuration](#-installation--configuration)
6. [💡 Usage](#-usage)
7. [🔐 Environment Variables](#-environment-variables)
8. [🤝 Contributing](#-contributing)
9. [📄 License](#-license)

</details>

<a id="-short-description"></a>
## 📌 Short Description
This project is a **Multi-Agent System (MAS)** built with [JADE](https://jade.tilab.com/) to simulate and optimize a smart physical intersection layout. It effectively manages two axes (`Axe_A` and `Axe_B`), autonomously adjusting traffic lights to maximize flow while ensuring immediate priority for emergency vehicles like ambulances. 

<a id="-key-features"></a>
## ✨ Key Features
- **Smart Traffic Lights**: Traffic lights communicate with each other via JADE agents to negotiate green light cycles, avoiding collisions and optimizing traffic.
- **Emergency Priority Handling**: Ambulances can override the normal cycle, forcing a green light instantly to clear the intersection.
- **Dynamic Traffic Generation**: Integrated simulation engine that randomly spawns cars (90%) and ambulances (10%) on both axes.
- **Live GUI Monitoring**: Provides a visual Real-time representation (Swing) of traffic light states and queue sizes.
- **Supervisor Tracking**: A dedicated supervisor agent monitors system Key Performance Indicators (KPIs) like total vehicle throughput and cars-per-second flow.

<a id="-project-architecture"></a>
## 🏗️ Project Architecture
```text
SMA_Traffic_Control/
├── src/main/java/ma/fstt/sma/
│   ├── AmbulanceAgent.java        # Agent overriding lights for emergencies
│   ├── CarAgent.java              # Agent representing standard vehicles
│   ├── SupervisorAgent.java       # Monitors the system & calculates traffic KPIs
│   ├── TrafficGeneratorAgent.java # Spawns Cars and Ambulances continuously
│   ├── TrafficGUI.java            # Swing-based graphical dashboard
│   └── TrafficLightAgent.java     # Core agent managing the intersection flow
├── pom.xml                        # Maven configuration and dependencies
└── APDescription.txt              # Agent Platform description
```

<a id="-prerequisites"></a>
## ⚙️ Prerequisites
Before running the project, ensure you have the following installed:
- **Java Development Kit (JDK) 21** or higher.
- **Apache Maven 3.6+**
- (Optional but recommended) An IDE like IntelliJ IDEA or VS Code, configured for Maven and Java.

<a id="-installation--configuration"></a>
## 🚀 Installation & Configuration
Clone the repository and compile the project using standard Maven commands.

```bash
# 1. Clone the repository (if applicable)
git clone https://github.com/yourusername/SMA_Traffic_Control.git
cd SMA_Traffic_Control

# 2. Clean and build the project
mvn clean install
```
Maven will automatically download the `com.tilab.jade:jade:4.6.0` dependency required for the MAS.

<a id="-usage"></a>
## 💡 Usage
The project relies on `exec-maven-plugin` mapped to the `jade.Boot` main class, booting the JADE platform and spawning the necessary agents automatically.

To launch the traffic simulation, run:
```bash
mvn exec:java
```

This will:
1. Start the JADE Agent Platform (and the RMA GUI).
2. Spawn **Axe_A** and **Axe_B** traffic lights.
3. Spawn the **Supervisor** and **Generator** agents.
4. Open the `TrafficGUI` window showing real-time traffic status.

You should see logs in your terminal indicating throughput and emergency events:
```text
🚨 [ALERTE] URGENCE AMBULANCE
🚦 [SYSTEM] Reprise du flux normal.
📊 [KPI] Total voitures: 42 | Flux: 1.25 v/s
```

<a id="-environment-variables"></a>
## 🔐 Environment Variables
No specific environment variables are required. 
All JADE agent configurations/arguments are pre-defined in the `pom.xml` under the `<arguments>` tag of the `exec-maven-plugin`:

```xml
<argument>
    Axe_A:ma.fstt.sma.TrafficLightAgent(Axe_B);
    Axe_B:ma.fstt.sma.TrafficLightAgent(Axe_A);
    Superviseur:ma.fstt.sma.SupervisorAgent;
    Generateur:ma.fstt.sma.TrafficGeneratorAgent
</argument>
```

<a id="-contributing"></a>
## 🤝 Contributing
Contributions, issues, and feature requests are welcome! 
1. Fork the project.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

<a id="-license"></a>
## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.