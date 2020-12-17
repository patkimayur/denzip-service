#!/bin/bash

getDateTime() {
echo $(date +"%m-%d-%Y_%H-%M")
}

uploadArtifact() {
artifact=$1
echo "Uploading artifact $artifact"
scp -i "RealEstateNinjas_Mumbai_Keypair.pem" $artifact ec2-user@ec2-13-127-93-182.ap-south-1.compute.amazonaws.com:/apps/stage/.

}

updateNginx() {
echo "Updating crs-nginx"
#now=$(date +"%m-%d-%Y_%H-%M")
now=$(getDateTime)
dir=crs-nginx.$now
mkdir $dir
artifact=$dir.tar.gz
cp -r /Users/YJ/CRS/crs-nginx-jainyogesh/* $dir/.
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
cp -r /Users/YJ/CRS/crs-rental-ui-jainyogesh/dist/crs-rental-ui/* $dir/.
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
cp -r /Users/YJ/CRS/feature-jainyogesh/crs-rental-config/target/classes/config/crs-13-234-166-235/* $dir/.
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
cp /Users/YJ/CRS/feature-jainyogesh/crs-rental-core/target/crs-rental-core-1.0.0-SNAPSHOT.jar $dir/crs-rental-uber.jar
tar -zcvf $artifact $dir
uploadArtifact $artifact
else
echo "Aborting..."
fi
}

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
