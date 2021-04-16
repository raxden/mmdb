TMP_DIR="/tmp"
BUNDLE_TOOL_JAR="$TMP_DIR/bundletool-all-1.4.0.jar"
BUNDLETOOL_EXEC="java -jar $BUNDLE_TOOL_JAR"
BUNDLE_DIR="./app/build/outputs/bundle/release/"
APKS_DIR="./app/build/outputs/bundle/release/"
ALIAS_NAME="mmdb"
APK_NAME="mmdb-release"
APK_PASS="bob1YTMqc5acHN9spcYI"

function generateAppBundle {
  echo "Generating app bundle"
  ./gradlew app:bundleRelease
}

# To install the application in your device, you need converting the bundle app generated in the last step to universal apk.
function generateUniversalAPKS {
  echo "Generating universal apks from app bundle..."
  $BUNDLETOOL_EXEC build-apks --overwrite --mode=universal --bundle=$BUNDLE_DIR/$APK_NAME.aab --output=$APKS_DIR/$APK_NAME.apks --ks=./config/release.jks --ks-pass=pass:$APK_PASS --ks-key-alias=$ALIAS_NAME --key-pass=pass:$APK_PASS
}

function installAPKS {
  echo "Installing, in device..."
  $BUNDLETOOL_EXEC install-apks --apks=$APKS_DIR/$APK_NAME.apks
  echo "... Installed"
}

function generateAPKS {
  generateAppBundle
  generateUniversalAPKS
  installAPKS
}

function downloadBundleTool {
  echo "Bundle tool wasn't found, prepare to download..."
  cd $TMP_DIR/ && { curl -O -L https://github.com/google/bundletool/releases/download/1.4.0/bundletool-all-1.4.0.jar; cd -; }
}

if test -f $BUNDLE_TOOL_JAR; then
  generateAPKS
else
  downloadBundleTool
  generateAPKS
fi
