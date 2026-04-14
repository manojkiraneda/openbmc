SUMMARY = "PMCI Test Service application"
DESCRIPTION = "A simple PMCI (Platform Management Component Intercommunication) test service application"
HOMEPAGE = "https://github.com/manojkiraneda/pmci-testservice"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01d1c2d8c3e6e7d6f1d3b3b3b3b3b3b3"

SRC_URI = "git://github.com/manojkiraneda/pmci-testservice.git;protocol=https;branch=main"

# Update SRCREV to the desired commit hash
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit meson

BBCLASSEXTEND = "native nativesdk"