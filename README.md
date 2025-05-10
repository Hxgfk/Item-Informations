# Item Informations Mod

[![CurseForge Downloads](https://img.shields.io/badge/dynamic/json?label=CurseForge&query=downloads.total&url=https://api.cfwidget.com/minecraft/mc-mods/iteminf&style=flat-square)](https://www.curseforge.com/minecraft/mc-mods/item-informations)
[![License](https://img.shields.io/badge/License-GPLv3-green.svg?style=flat-square)](LICENSE)

A lightweight utility mod that provides detailed item inspection through simple commands.

## ✨ Features

- **Instant Item Analysis**  
  Use `/iteminf` command to scan held items
- **Comprehensive Data**  
  Displays:
  - Basic item metadata (ID, registry name)
  - Detailed NBT data (compact or formatted)
  - Food properties (hunger restoration, saturation, effects)
  - Tool/Weapon stats (durability, attack damage, mining level)
  - Equipment type classification (pickaxe, sword, etc.)
- **Developer Friendly**  
  Copy-paste ready NBT output for debugging
- **Customizable Output**  
  Configure displayed information via config file
- **Multi-loader Support**  
  Available for both Forge and Fabric

## 🕹 Usage

1. Hold any item in main hand
2. Open chat and type:  
   `/iteminf`
3. Get detailed information:  
   Example Output:
 ```text

```
   *(Screenshot placeholder - you should add real image later)*

### Advanced Commands
| Command | Description |
|---------|-------------|
| `/iteminf nbt` | Raw NBT output |
| `/iteminf food` | Detailed nutrition info |
| `/iteminf tool` | Durability analysis |

## ⚙ Configuration

Edit `config/iteminf-common.toml` to customize:

```toml
[client]
# Show/hide information categories
showNBT = true
showDurability = true
showFoodStats = true

# Output formatting
compactMode = false 
maxNBTDepth = 3
```
## 📦 Compatibility
| Minecraft Version | Status | Loader |
|-------------------|-----|--------|
| 1.20.1            | ✔️ | Forge |
| 1.19.4            | ✔️ | Forge |
| 1.18.2            | ⚠️ | Forge |

## 📜 License
This project is licensed under the GNU GPL 3.0 License.

## ❓ Support
Report issues on GitHub Issues
Mod ID: iteminf