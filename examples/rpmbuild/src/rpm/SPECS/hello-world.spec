Name:       hello-world
Version:    %{ver}
Release:    %{rel}
Summary:    A simple RPM package
License:    Proprietary
Source0:    hello-world.sh

%description
This is my first RPM package, which does nothing.

%prep
# Intentionally left blank.

%install
mkdir -p %{buildroot}/usr/bin/
install -m 755 %{SOURCE0} %{buildroot}/usr/bin/hello-world.sh

%files
/usr/bin/hello-world.sh
