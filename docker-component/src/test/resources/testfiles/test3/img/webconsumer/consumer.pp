# Clone repo
vcsrepo { '/usr/src/app':
  ensure   => present,
  provider => git,
  source   => 'git://github.com/wtelecom/crane-tests-webconsumer.git',
}

# Install requirements
class { 'python' :
  version    => 'system',
  pip        => 'present',
  dev        => 'present',
}

exec { "requirements":
    command => "pip install -r requirements.txt",
    cwd     => "/usr/src/app",
    path    => "/usr/local/bin/:/bin/:/usr/bin",
    require => Class["python"]
}
