# tracking what the player is riding

modern Forge versions (at least 1.12): have **events** that are fired when an entity is mounted or dismounted

modern Fabric versions: **don't have events** as far as i can tell, but easily implementable in mixin (done with `LocalPlayerMixin` here) 

old Forge versions: **don't have events** and coremods/mixin are going to be ass to set up

every version: possible to fall back to a **ticker-based approach**, only primitive this needs is a "get current vehicle"

# camera type handling

modern versions: enum (0: FIRST_PERSON_BACK, 1: THIRD_PERSON_BACK, 2: THIRD_PERSON_FRONT)

old versions: `int` with same values

modern versions: cycle is modifiable with a mixin to `CameraType#cycle`

every version: register a ticker (preferably as early as possible *before* camera setup) and set camera type to firstperson if it's thirdperson front

# architecture

previous version: kinda botania style, two "service" classes are required in the constructor: one for interacting with minecraft & one for interacting with the loader. A little bit wrong (like "logging" requires interacting with FML on 1.4.7, so it's not really a vanilla-only capability) and I don't really think the separation proves useful.

better approach imo: one mod class full of abstract methods, extended abstractly in the xplat module with whatever is possible to implement there, and further extended in the loader module

Tweaks:

* Would be nice if the `State` type was pluggable, especially for the old versions of forge
* Remove the "client tickers" thing in favor of "making the loader call AutoThirdPerson#tick"