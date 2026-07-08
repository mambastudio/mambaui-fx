# MambaUI FX

Reusable JavaFX controls, layouts, dialogs, and theme helpers.

MambaUI FX is a small, curated Java 25 component library for JavaFX applications. It is intended to collect polished controls that are useful across projects instead of leaving them scattered through application-specific side projects.

## Components

- `Tile` - compact header/description layout with optional left and right nodes.
- `PlaceholderTreeView` - `TreeView` variant that can show a placeholder when no root is present.
- `ModalDialog` - lightweight modal dialog control with header, content, footer, resizing, and result handling.
- `ModalDialogs` - convenience factories for common confirmation, information, and error dialogs.
- `RectCuts` - immutable rectangle slicing helpers based on the RectCut approach.
- `IconButtons` - small factories for Ikonli-backed JavaFX buttons.
- `MambauiTheme` - helpers for applying bundled JavaFX stylesheets.

## Requirements

- Java 25 with preview features enabled. The Maven build enforces Java 25.
- JavaFX 26 early-access dependencies.
- Maven 3.9 or newer.

The project intentionally targets Java 25 for now. If a newer JavaFX release raises its JDK minimum, this project can move with it.

## Modules

```text
mambaui-fx-parent
  mambaui-fx       reusable JavaFX library
  mambaui-fx-demo  visual demo/sample applications
```

The library and demo are separate Maven modules. The demo module depends on the library module, so sample apps can use the public API exactly like another application would.

## Build

```powershell
mvn package
```

The root build compiles both modules. The library jar, sources jar, and javadoc jar are created under `mambaui-fx/target/`.

## Use Locally

Install into your local Maven repository:

```powershell
mvn install
```

Then add:

```xml
<dependency>
    <groupId>com.mamba</groupId>
    <artifactId>mambaui-fx</artifactId>
    <version>0.1</version>
</dependency>
```

For JPMS applications:

```java
requires com.mamba.mambaui;
```

## Example

```java
import com.mamba.mambaui.control.PlaceholderTreeView;
import com.mamba.mambaui.control.Tile;
import com.mamba.mambaui.modal.ModalDialog;
import com.mamba.mambaui.modal.ModalDialogs;

Tile tile = new Tile("Account", "Manage account details and preferences");
tile.setRight(new Button("Open"));

PlaceholderTreeView<String> treeView = new PlaceholderTreeView<>();
treeView.setPlaceholder(new Label("No items yet"));

ModalDialog<Boolean> dialog = ModalDialogs.confirm("Continue?");
```

## Project Shape

```text
mambaui-fx/src/main/java/com/mamba/mambaui
  control/    reusable JavaFX controls
  geometry/   rectangle slicing primitives
  icon/       icon and button helpers
  internal/   skins and implementation details, not exported
  modal/      modal dialog API and implementations
  theme/      theme helpers

mambaui-fx/src/main/resources/com/mamba/mambaui
  theme/      shared stylesheets
  modal/      modal dialog stylesheet

mambaui-fx-demo/src/main/java/com/mamba/mambaui/samples
  closebutton/ visual close button sample
  modal/       visual modal dialog sample
  tile/        visual tile sample
```

The demo applications live in their own module instead of test packages. This keeps the library artifact clean while still making samples easy to open, run, and refactor in IDEs such as NetBeans.

## Roadmap

- Add a showcase/demo application with screenshots.
- Add `SVGNodeIcon` and other reusable icon helpers.
- Add focused tests for layout helpers and control properties.
- Track the latest JavaFX/JDK baseline as JavaFX evolves.
- Publish snapshots through GitHub Packages or JitPack before considering Maven Central.
