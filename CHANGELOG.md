# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).
Prior to version 6.0.0, this project used MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH.

## [10.0.1+1.21.8] - 2026.03.03
### Changed
- Updated to Minecraft 1.21.8

## [10.0.1+1.21.4] - 2025.07.23
### Changed
- Updated Caelus API requirement to 8.0.1 to fix an issue with helmets being damaged while gliding with an elytra
in a curio slot

## [10.0.0+1.21.4] - 2025.06.20
### Added
- Native compatibility with all items that use the `minecraft:glider` data component
### Changed
- Updated to Minecraft 1.21.4

## [9.0.2+1.21.1] - 2024.10.02
### Fixed
- Fixed crash with Deeper and Darker integration [#136](https://github.com/illusivesoulworks/elytraslot/issues/136)

## [9.0.1+1.21.1] - 2024.09.30
### Changed
- Updated to Minecraft 1.21.1
### Fixed
- Fixed Deeper Darker Soul Elytra boosting [#132](https://github.com/illusivesoulworks/elytraslot/issues/132)
- Fixed crash with newer versions of MixinSquared [#135](https://github.com/illusivesoulworks/elytraslot/issues/135)
- Fixed potential ConcurrentModificationException upon joining a world [#130](https://github.com/illusivesoulworks/elytraslot/issues/130)

## [9.0.0+1.21] - 2024.07.17
### Changed
- Updated to Minecraft 1.21

## [8.0.1+1.20.6] - 2024.07.16
### Fixed
- [Fabric] Fixed vanilla elytra not consuming durability while in Trinkets slots [#119](https://github.com/illusivesoulworks/elytraslot/issues/119)

## [8.0.0+1.20.6] - 2024.06.12
### Changed
- Updated to Minecraft 1.20.6

## [7.0.0+1.20.4] - 2024.06.12
### Changed
- Updated to Minecraft 1.20.4

## [6.4.0+1.20.1] - 2024.06.12
### Added
- Added Mythic Metals compatibility [#79](https://github.com/illusivesoulworks/elytraslot/issues/79)
- Added Elytra Bounce compatibility [#77](https://github.com/illusivesoulworks/elytraslot/issues/77)
- Added LieOnLion's Enderite compatibility [#88](https://github.com/illusivesoulworks/elytraslot/issues/88)
- Added Wooden Elytra compatibility [#68](https://github.com/illusivesoulworks/elytraslot/issues/68)
- Added Aileron compatibility [#60](https://github.com/illusivesoulworks/elytraslot/issues/60)
- Added Wavey Capes compatibility [#32](https://github.com/illusivesoulworks/elytraslot/issues/32)
### Changed
- Improved Deeper and Darker compatibility [#102](https://github.com/illusivesoulworks/elytraslot/issues/102)
### Fixed
- Fixed Mana and Artifice compatibility [#34](https://github.com/illusivesoulworks/elytraslot/issues/34)
- Fixed render toggling [#97](https://github.com/illusivesoulworks/elytraslot/issues/97)
- Fixed elytra unequipping on reload [#112](https://github.com/illusivesoulworks/elytraslot/issues/112)

## [6.3.1+1.20.1] - 2023.07.24
### Added
- Added Clutter compatibility

## [6.3.0+1.20.1] - 2023.06.20
### Changed
- Updated to Minecraft 1.20.1

## [6.2.1+1.19.4] - 2023.05.02
### Changed
- Updated to Minecraft 1.19.4
### Fixed
- Fixed potential crash in certain environments [#73](https://github.com/illusivesoulworks/elytraslot/issues/73)

## [6.2.0+1.19.3] - 2023.03.13
### Added
- Added Quilt support
### Changed
- Updated to Minecraft 1.19.3
- [Forge] Updated to Curios 1.19.3-5.1.2.0
- [Forge] Updated to Caelus 1.19.3-3.0.0.7

## [6.1.0+1.19.2] - 2023.02.01
### Added
- Added Lil' Wings compatibility [#62](https://github.com/illusivesoulworks/elytraslot/issues/62)
- Added Enderite Mod compatibility [#59](https://github.com/illusivesoulworks/elytraslot/issues/59)
- Added Deeper and Darker compatibility [#57](https://github.com/illusivesoulworks/elytraslot/issues/57)
- Added MinecraftCapes compatibility [#48](https://github.com/illusivesoulworks/elytraslot/issues/48)
- Added Mekanism compatibility [#44](https://github.com/illusivesoulworks/elytraslot/issues/44)
- Added Alex's Mobs compatibility [#28](https://github.com/illusivesoulworks/elytraslot/issues/28)

## [6.0.0+1.19.2] - 2022.08.11
### Changed
- Updated to Minecraft 1.19.2
- [Forge] Updated to Forge 43+
- [Fabric] Updated to Fabric API 0.59.0+

## [6.0.0+1.19.1] - 2022.07.29
### Changed
- Updated to Minecraft 1.19.1
- [Forge] Updated to Forge 42+
- [Forge] Updated to Caelus 1.19.1-3.0.0.4+
- [Forge] Updated to Curios 1.19.1-5.1.0.5+
- [Fabric] Updated to Fabric API 0.58.5+

## [6.0.0-beta.1+1.19] - 2022.07.20
### Changed
- Merged Forge and Fabric versions of the project together using the [MultiLoader template](https://github.com/jaredlll08/MultiLoader-Template)
- Changed to [Semantic Versioning](http://semver.org/spec/v2.0.0.html)
- Updated to Minecraft 1.19
- [Forge] Updated to Forge 41+
- [Forge] Updated to Caelus 1.19-3.0.0.3+
- [Forge] Updated to Curios 1.19-5.1.0.0+
- [Fabric] Updated to Fabric API 0.55.2+
- [Fabric] Updated to Trinkets 3.4.0+

## [1.18.1-5.0.1.0] - 2022.01.11
### Added
- Added support for Quark's Colored Runes [#40](https://github.com/TheIllusiveC4/CuriousElytra/issues/40)
### Fixed
- Fixed rendering errors and log spam with Silent Gear [#39](https://github.com/TheIllusiveC4/CuriousElytra/issues/39)
- Fixed missing Caelus API requirement in `mods.toml`

## [1.18.1-5.0.0.1] - 2021.12.16
### Fixed
- Fixed duplicate capability key errors [#37](https://github.com/TheIllusiveC4/CuriousElytra/issues/37)

## [1.18.1-5.0.0.0] - 2021.12.15
### Changed
- Updated to Minecraft 1.18.1

## [1.17.1-5.0.0.0] - 2021.12.15
### Changed
- Updated to Minecraft 1.17.1
- Updated to Forge 37.0+
- Updated to Curios 1.17.1-5.0+
- Updated to Caelus 1.17.1-3.0+
### Removed
- Removed compatibility for mods that are not on 1.17.1

## [1.16.5-4.0.2.4] - 2021.11.10
### Fixed
- Fixed compatibility with modded elytras so that rendering works correctly
- Fixed Spectral Elytra compatibility

## [1.16.5-4.0.2.3] - 2021.03.07
### Fixed
- Fixed elytra ticking logic [#17](https://github.com/TheIllusiveC4/CuriousElytra/issues/17)

## [1.16.5-4.0.2.2] - 2021.03.07
### Changed
- Refactored some logic for the elytra curio

## [1.16.5-4.0.2.1] - 2021.03.02
### Fixed
- Fixed elytras working while broken [#16](https://github.com/TheIllusiveC4/CuriousElytra/issues/16)

## [1.16.5-4.0.2.0] - 2021.01.21
### Added
- Added Silent Gear Elytra rendering integration

## [1.16.5-4.0.1.0] - 2021.01.21
### Added
- Added Enderite Mod integration
- Added Netherite Plus integration
### Changed
- Updated to Minecraft 1.16.5

## [1.16.3-4.0.0.1] - 2020.09.27
### Changed
- Updated to Minecraft 1.16.3

## [1.16.2-4.0.0.0] - 2020.08.14
### Changed
- Updated to Minecraft 1.16.2

## [1.16.1-3.0.0.0] - 2020.07.02
### Changed
- Updated to Minecraft 1.16.1

## [1.15.2-2.0.0.0] - 2019.09.15
### Changed
- Updated to Minecraft 1.15.2

## [1.14.4-1.0.0.0] - 2019.09.15
### Changed
- Updated to Forge 28.1.0+

## [1.14.4-0.0.0.7] - 2019.07.24
### Changed
- Updated to Minecraft 1.14.4

## [1.14.3-0.0.0.6] - 2019.07.07
### Changed
- Updated to Caelus 0.7+

## [1.14.3-0.0.0.5] - 2019.07.05
### Changed
- Updated to Minecraft 1.14.3

## [1.13.2-0.0.0.4] - 2019.06.16
### Changed
- Updated to last Forge and mappings for 1.13.2
### Fixed
- Fixed dedicated server crash [#1](https://github.com/TheIllusiveC4/CuriousElytra/issues/1)

## [1.13.2-0.0.0.3] - 2019.04.11
### Changed
- Updated to Curios 0.10+

## [1.13.2-0.0.0.2] - 2019.03.29
### Fixed
- Fixed broken elytras still giving flight

## [1.13.2-0.0.0.1] - 2019.03.19
- Initial beta release
