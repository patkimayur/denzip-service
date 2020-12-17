#!/bin/bash

getDateTime() {
echo $(date +"%m-%d-%Y_%H-%M")
}

uploadArtifact() {
artifact=$1

if [[($server == "prod1")]]; then
echo "Uploading artifact $artifact to production server prod1"
scp $artifact crs_user@172.31.26.127:/apps/stage/.
elif [[($server == "prod3")]]; then
echo "Uploading artifact $artifact to production server prod3"
scp $artifact crs_user@172.31.21.163:/apps/stage/.
fi

}

updateNginx() {
echo "Updating crs-nginx"
#now=$(date +"%m-%d-%Y_%H-%M")
now=$(getDateTime)
dir=crs-nginx.$now
mkdir $dir
artifact=$dir.tar.gz
cd /apps/crs/codebase/crs-nginx
git tag -a $dir -m "Creating Tag Verion $dir"
git push origin : $dir
cd -
cp -r /apps/crs/codebase/crs-nginx/* $dir/.
tar -zcvf $artifact $dir
uploadArtifact $artifact
}

updateUI() {
echo "Updating crs-rental-ui. Please make sure ng build --prod has been done before this command. Type Y to continue"
read input
if [[($input == "Y" || $input == "y")]]; then
now=$(getDateTime)
dir=crs-rental-ui.$now
mkdir $dir
artifact=$dir.tar.gz
cd /apps/crs/codebase/crs-rental-ui
git tag -a $dir -m "Creating Tag Verion $dir"
git push origin : $dir
cd -
cp -r /apps/crs/codebase/crs-rental-ui/dist/* $dir/.
tar -zcvf $artifact $dir
uploadArtifact $artifact
else
echo "Aborting..."
fi
}

updateConfig() {
echo "Updating crs-rental-config. Please make sure config build is done before this command. Type Y to continue"
read input
if [[($input == "Y" || $input == "y")]]; then
now=$(getDateTime)
dir=crs-rental-config.$now
mkdir $dir
artifact=$dir.tar.gz
cd /apps/crs/codebase/crs-rental-service
git tag -a $dir -m "Creating Tag Verion $dir"
git push origin : $dir
cd -
if [[($server == "prod1")]]; then
cp -r /apps/crs/codebase/crs-rental-service/crs-rental-config/target/classes/config/crs-172.31.26.127-prod1/* $dir/.
elif [[($server == "prod2")]]; then
cp -r /apps/crs/codebase/crs-rental-service/crs-rental-config/target/classes/config/crs-172.31.21.163-prod3/* $dir/.
fi
tar -zcvf $artifact $dir
uploadArtifact $artifact
else
echo "Aborting..."
fi
}

updateUber() {
echo "Updating crs-rental-uber. Please make sure uber build is done before this command. Type Y to continue"
read input
if [[($input == "Y" || $input == "y")]]; then
now=$(getDateTime)
dir=crs-rental-service.$now
mkdir $dir
artifact=$dir.tar.gz
cd /apps/crs/codebase/crs-rental-service
git tag -a $dir -m "Creating Tag Verion $dir"
git push origin : $dir
cd -
cp /apps/crs/codebase/crs-rental-service/crs-rental-core/target/crs-rental-core-1.0.0-SNAPSHOT.jar $dir/crs-rental-uber.jar
tar -zcvf $artifact $dir
uploadArtifact $artifact
else
echo "Aborting..."
fi
}

echo "Please enter which production server should be update - prod1 / prod3"
read server
echo "Production Server selected is $server"
echo "Please enter what should be updated - nginx / ui / config / uber"
read option
echo "Option selected is $option"
if [[($option == "nginx")]]; then
updateNginx
elif [[($option == "ui")]]; then
updateUI
elif [[($option == "config")]]; then
updateConfig
elif [[($option == "uber")]]; then
updateUber
else
echo "Invalid option"
fi
