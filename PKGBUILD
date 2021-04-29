pkgname=bakup
pkgver=1.0.0
pkgrel=1
arch=('any')
licence=('custom')
depends=('java-environment>=11')
source=("https://github.com/mjaakko/BakUp/releases/download/$pkgver/bakup.tar.gz")
md5sums=('SKIP')

package() {
  mkdir -p $pkgdir/usr/share/java/bakup
  cp $srcdir/bakup.jar $pkgdir/usr/share/java/bakup
  mkdir -p $pkgdir/usr/bin
  cp $srcdir/bakup $pkgdir/usr/bin
}
