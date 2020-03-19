def call(Map config) {
    String specfile = "target/rpmbuild-tmp/SPECS/${config.specfile}"

    sh """
        mkdir -p target/rpmbuild-tmp/{BUILD,RPMS,SOURCES,SPECS,SRPMS}
        cp -r "${config.topdir}" "target/rpmbuild-tmp"

        rpmbuild -bb \
            --define "_topdir target/rpmbuild-tmp" \
            --define "ver ${config.version}" \
            --define "rel ${config.release}" \
            $specfile
    """
}
