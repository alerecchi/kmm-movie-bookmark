//
//  DI.swift
//  iosApp
//
//  Created by feanor on 01/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Swinject
import Foundation

final class DI {

	private var assembler: Assembler?

	static let shared = DI()

	private init() {}

	func register(assemblies: [Assembly]) {
		assembler = Assembler(assemblies)
	}

	func resolve<T>(_ type: T.Type, name: String? = nil) -> T {

		guard let object =  assembler?.resolver.resolve(type, name: name) else {
			fatalError()
		}

		return object
	}

}
