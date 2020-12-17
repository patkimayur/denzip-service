#!/bin/bash

syncNginx() {
artifact=$1
echo "Syncing crs-nginx with $artifact"
cp /apps/stage/$artifact artifacts/.
cd artifacts
artifactDir=`tar -tzf $artifact | head -1 | cut -f1 -d"/"`
echo $artifactDir
tar -zxvf $artifact
cd ..
rm crs-nginx.previous
mv crs-nginx crs-nginx.previous
ln -fs artifacts/$artifactDir crs-nginx
}

syncUI() {
artifact=$1
echo "Syncing crs-rental-ui with $artifact"
cp /apps/stage/$artifact artifacts/.
cd artifacts
artifactDir=`tar -tzf $artifact | head -1 | cut -f1 -d"/"`
echo $artifactDir
tar -zxvf $artifact
cd ..
rm crs-rental-ui.previous
mv crs-rental-ui crs-rental-ui.previous
ln -fs artifacts/$artifactDir crs-rental-ui
}

syncConfig() {
artifact=$1
echo "Syncing crs-rental-config with $artifact"
cp /apps/stage/$artifact artifacts/.
cd artifacts
artifactDir=`tar -tzf $artifact | head -1 | cut -f1 -d"/"`
echo $artifactDir
tar -zxvf $artifact
cd ..
rm crs-rental-config.previous
mv crs-rental-config crs-rental-config.previous
ln -fs artifacts/$artifactDir crs-rental-config

}

syncUber() {
artifact=$1
echo "Syncing crs-rental-service with $artifact"
cp /apps/stage/$artifact artifacts/.
cd artifacts
artifactDir=`tar -tzf $artifact | head -1 | cut -f1 -d"/"`
echo $artifactDir
tar -zxvf $artifact
cd ..
rm crs-rental-service.previous
mv crs-rental-service crs-rental-service.previous
ln -fs artifacts/$artifactDir crs-rental-service

}


echo "Please enter what should be synced - nginx / ui / config / uber"
read option
echo "Option selected is $option"
if [[($option == "nginx")]]; then
echo "Exact artifact name present in stage dir"
read artifact
echo "Syncing $artifact"
syncNginx $artifact
elif [[($option == "ui")]]; then
echo "Exact artifact name present in stage dir"
read artifact
echo "Syncing $artifact"
syncUI $artifact
elif [[($option == "config")]]; then
echo "Exact artifact name present in stage dir"
read artifact
echo "Syncing $artifact"
syncConfig $artifact
elif [[($option == "uber")]]; then
echo "Exact artifact name present in stage dir"
read artifact
echo "Syncing $artifact"
syncUber $artifact
else
echo "Invalid option"
fi
