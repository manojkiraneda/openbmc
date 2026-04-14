SUMMARY = "DMTF PMCI Test Tools Interface (PTTI) test service library"
DESCRIPTION = "A sample implementation of a DSP0280 PMCI Test Tools Interface (PTTI) \
version 1.0 test service that runs on a control plane (such as a BMC). This library \
should be able to be integrated into various vendors' control planes via a set of \
well-defined interfaces."
HOMEPAGE = "https://github.com/DMTF/lib-test-service"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=9b6c20a0e8e00db8a4c9b5c9e5c8e5e5"

SRC_URI = "git://github.com/DMTF/lib-test-service.git;protocol=https;branch=main"

# Update SRCREV to the desired commit hash
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Dependencies
DEPENDS = ""

# Build flags
EXTRA_OEMAKE = " \
    'CC=${CC}' \
    'CFLAGS=${CFLAGS} -std=c11 -Wall -Iinclude/libtestsvc/ -IlibPTTI/ -DENABLE_DEBUG_PRINTS' \
"

do_compile() {
    # Create build directory
    mkdir -p ${B}/build
    
    # Compile libtestsvc.o
    ${CC} ${CFLAGS} -c -std=c11 -Wall -Iinclude/libtestsvc/ -IlibPTTI/ \
        -DENABLE_DEBUG_PRINTS ${S}/src/libtestsvc.c -o ${B}/build/libtestsvc.o
    
    # Compile ptti.o
    ${CC} ${CFLAGS} -c -std=c11 -Wall -Iinclude/libtestsvc/ -IlibPTTI/ \
        -DENABLE_DEBUG_PRINTS ${S}/libPTTI/ptti.c -o ${B}/build/ptti.o
    
    # Create static library
    ${AR} rcs ${B}/build/libtestsvc.a ${B}/build/libtestsvc.o ${B}/build/ptti.o
    
    # Create shared library
    ${CC} ${CFLAGS} -shared -fPIC -Wl,-soname,libtestsvc.so.1 \
        ${B}/build/libtestsvc.o ${B}/build/ptti.o \
        -o ${B}/build/libtestsvc.so.1.0.0 -lpthread
    
    # Create symlinks for shared library
    cd ${B}/build
    ln -sf libtestsvc.so.1.0.0 libtestsvc.so.1
    ln -sf libtestsvc.so.1 libtestsvc.so
}

do_install() {
    # Install libraries
    install -d ${D}${libdir}
    install -m 0644 ${B}/build/libtestsvc.a ${D}${libdir}/
    install -m 0755 ${B}/build/libtestsvc.so.1.0.0 ${D}${libdir}/
    ln -sf libtestsvc.so.1.0.0 ${D}${libdir}/libtestsvc.so.1
    ln -sf libtestsvc.so.1 ${D}${libdir}/libtestsvc.so
    
    # Install headers
    install -d ${D}${includedir}/libtestsvc
    install -m 0644 ${S}/include/libtestsvc/libtestsvc.h ${D}${includedir}/libtestsvc/
    
    install -d ${D}${includedir}/libPTTI
    install -m 0644 ${S}/libPTTI/ptti.h ${D}${includedir}/libPTTI/
}

# Package the library
FILES:${PN} = "${libdir}/libtestsvc.so.*"
FILES:${PN}-dev = "${includedir} ${libdir}/libtestsvc.so ${libdir}/libtestsvc.a"
FILES:${PN}-staticdev = "${libdir}/libtestsvc.a"

BBCLASSEXTEND = "native nativesdk"