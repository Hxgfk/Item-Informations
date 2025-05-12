# Item Informations Mod

[![CurseForge Downloads](https://img.shields.io/badge/dynamic/json?label=CurseForge&query=downloads.total&url=https://api.cfwidget.com/minecraft/mc-mods/item-informations&style=flat-square)](https://www.curseforge.com/minecraft/mc-mods/item-informations)
[![License](https://img.shields.io/badge/License-GPLv3-green.svg?style=flat-square)](LICENSE)

A lightweight utility mod that provides detailed item inspection through simple commands.

## ✨ Features

- **Instant Item Analysis**  
  Use `/iteminf` command to scan held items
- **Comprehensive Data**  
  Displays:
  - Basic item metadata (ID, registry name)
  - Detailed NBT data (formatted)
  - Food properties (hunger restoration, saturation, effects)
  - Tool/Weapon stats (durability, attack damage, mining level)
  - Equipment type classification (pickaxe, sword, etc.)

## 🕹 Usage

1. Hold any item in main hand
2. Open chat and type:  
   `/iteminf`
3. Get detailed information:  
   Example Output:
 ```text
Item: Apple
ID: minecraft:apple
NBT: {tag: 10}
Food Information:

```

### Advanced Commands
| Command         | Description                   |
|-----------------|-------------------------------|
| `/iteminf nbt`  | NBT data                      |
| `/iteminf food` | Food data                     |
| `/iteminf tool` | Tool data                     |
| `/iteminf base` | Base information of this item |

## 📦 Compatibility
| Minecraft Version | Status | Loader |
|-------------------|--------|--------|
| 1.20.1            |   ✔️   | Forge  |

## 📜 License
This project is licensed under the GNU GPL 3.0 License.

## ❓ Support
Report issues on GitHub Issues
Mod ID: iteminf