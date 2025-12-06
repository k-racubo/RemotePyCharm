# RemotePyCharm - android client
> Mobile client for remote PyCharm IDE sessions

## Quick start
### Build from source
> java 11+ required

```bash
# create folder, then:
cd folder_name

# clone metadata only
git clone --branch android-app --filter=blob:none --no-checkout --sparse https://github.com/k-racubo/RemotePyCharm.git .

# pull only android app folder
git sparse-checkout set apps/androidApp
git checkout

cd apps/androidApp

# Create local.properties with Android SDK path
echo "sdk.dir=/path/to/your/android/sdk" > local.properties
```

Then:

- Open in (Android Studio || Intelij IDEA)
- Sync Project with Gradle Files
- If issues → File → Invalidate Caches & Restart
- Build: ./gradlew build (Linux/Mac) or gradlew.bat build (Windows)